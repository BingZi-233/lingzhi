package com.lingzhi.async.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingzhi.async.dto.*;
import com.lingzhi.async.entity.AsyncTask;
import com.lingzhi.async.enums.TaskStatus;
import com.lingzhi.async.mapper.AsyncTaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 异步任务服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncTaskServiceImpl implements AsyncTaskService {

    private final AsyncTaskMapper asyncTaskMapper;
    private final ApplicationContext applicationContext;
    
    // 默认线程池（从现有的 AsyncServiceImpl 获取）
    private ThreadPoolTaskExecutor defaultExecutor;
    
    // 任务结果缓存（短期缓存，用于获取结果）
    private final ConcurrentHashMap<String, CompletableFuture<Object>> taskFutureMap = new ConcurrentHashMap<>();

    // 设置默认线程池（通过后置处理器注入）
    public void setDefaultExecutor(ThreadPoolTaskExecutor executor) {
        this.defaultExecutor = executor;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AsyncTask createTask(TaskCreateRequest request, Long creatorId, String creatorName) {
        // 生成 taskKey
        String taskKey = request.getTaskKey();
        if (taskKey == null || taskKey.isBlank()) {
            taskKey = UUID.randomUUID().toString().replace("-", "");
        }

        // 检查 taskKey 是否已存在
        AsyncTask existingTask = asyncTaskMapper.selectByTaskKey(taskKey);
        if (existingTask != null) {
            throw new RuntimeException("任务标识已存在: " + taskKey);
        }

        // 创建任务实体
        AsyncTask task = new AsyncTask();
        task.setTaskKey(taskKey);
        task.setTaskName(request.getTaskName());
        task.setTaskType(request.getTaskType());
        task.setParams(request.getParams());
        task.setStatus(TaskStatus.PENDING.getCode());
        task.setProgress(0);
        task.setExecutorName(request.getExecutorName());
        task.setTimeout(request.getTimeout());
        task.setExpectedDuration(request.getExpectedDuration());
        task.setCreatorId(creatorId);
        task.setCreatorName(creatorName);
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        task.setDeleted(0);

        asyncTaskMapper.insert(task);
        log.info("创建异步任务: taskKey={}, taskName={}, taskType={}", taskKey, request.getTaskName(), request.getTaskType());
        
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AsyncTask createAndExecuteTask(TaskCreateRequest request, Callable<Object> callable,
                                           Long creatorId, String creatorName) {
        // 先创建任务
        AsyncTask task = createTask(request, creatorId, creatorName);
        
        // 异步执行
        executeTaskAsync(task.getTaskKey(), callable, request.getExecutorName(), request.getTimeout());
        
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeTask(String taskKey, Callable<Object> callable) {
        AsyncTask task = asyncTaskMapper.selectByTaskKey(taskKey);
        if (task == null) {
            throw new RuntimeException("任务不存在: " + taskKey);
        }
        
        executeTaskAsync(taskKey, callable, task.getExecutorName(), task.getTimeout());
    }

    @Override
    public Object executeTaskWithResult(String taskKey, Callable<Object> callable, long timeout) {
        AsyncTask task = asyncTaskMapper.selectByTaskKey(taskKey);
        if (task == null) {
            throw new RuntimeException("任务不存在: " + taskKey);
        }
        
        // 如果任务已执行完成，直接返回结果
        if (task.getStatus() == TaskStatus.SUCCESS.getCode()) {
            return task.getResult();
        }
        if (task.getStatus() == TaskStatus.FAILED.getCode()) {
            throw new RuntimeException("任务执行失败: " + task.getErrorMsg());
        }
        
        // 创建 CompletableFuture
        CompletableFuture<Object> future = new CompletableFuture<>();
        taskFutureMap.put(taskKey, future);
        
        try {
            executeTaskAsync(taskKey, callable, task.getExecutorName(), timeout);
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // 超时更新任务状态
            updateTimeout(taskKey);
            future.completeExceptionally(e);
            throw new RuntimeException("任务执行超时", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.completeExceptionally(e);
            throw new RuntimeException("任务执行被中断", e);
        } catch (ExecutionException e) {
            future.completeExceptionally(e);
            throw new RuntimeException("任务执行失败: " + e.getCause().getMessage(), e.getCause());
        } finally {
            taskFutureMap.remove(taskKey);
        }
    }

    /**
     * 异步执行任务
     */
    private void executeTaskAsync(String taskKey, Callable<Object> callable, 
                                   String executorName, Long timeout) {
        // 更新任务状态为执行中
        AsyncTask updateTask = new AsyncTask();
        updateTask.setStatus(TaskStatus.RUNNING.getCode());
        updateTask.setStartTime(LocalDateTime.now());
        
        LambdaQueryWrapper<AsyncTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsyncTask::getTaskKey, taskKey);
        asyncTaskMapper.update(updateTask, wrapper);

        // 获取线程池
        ThreadPoolTaskExecutor executor = getExecutor(executorName);

        // 提交任务
        CompletableFuture.runAsync(() -> {
            LocalDateTime startTime = LocalDateTime.now();
            try {
                Object result = callable.call();
                
                // 更新成功状态
                AsyncTask successTask = new AsyncTask();
                successTask.setStatus(TaskStatus.SUCCESS.getCode());
                successTask.setResult(result);
                successTask.setProgress(100);
                successTask.setEndTime(LocalDateTime.now());
                successTask.setActualDuration(
                    java.time.Duration.between(startTime, LocalDateTime.now()).toMillis()
                );
                
                LambdaQueryWrapper<AsyncTask> successWrapper = new LambdaQueryWrapper<>();
                successWrapper.eq(AsyncTask::getTaskKey, taskKey);
                asyncTaskMapper.update(successTask, successWrapper);
                
                // 设置 CompletableFuture 结果
                CompletableFuture<Object> future = taskFutureMap.get(taskKey);
                if (future != null) {
                    future.complete(result);
                }
                
                log.info("任务执行成功: taskKey={}", taskKey);
                
            } catch (Exception e) {
                // 更新失败状态
                updateFailed(taskKey, e.getMessage());
                
                // 设置 CompletableFuture 异常
                CompletableFuture<Object> future = taskFutureMap.get(taskKey);
                if (future != null) {
                    future.completeExceptionally(e);
                }
                
                log.error("任务执行失败: taskKey={}, error={}", taskKey, e.getMessage(), e);
            }
        }, executor);

        // 如果设置了超时，启动超时检查
        if (timeout != null && timeout > 0) {
            executor.execute(() -> {
                try {
                    Thread.sleep(timeout);
                    // 检查任务是否还在执行
                    AsyncTask task = asyncTaskMapper.selectByTaskKey(taskKey);
                    if (task != null && task.getStatus() == TaskStatus.RUNNING.getCode()) {
                        updateTimeout(taskKey);
                        CompletableFuture<Object> future = taskFutureMap.get(taskKey);
                        if (future != null) {
                            future.completeExceptionally(new TimeoutException("任务执行超时"));
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    @Override
    public TaskDetailResponse getTaskDetail(String taskKey) {
        AsyncTask task = asyncTaskMapper.selectByTaskKey(taskKey);
        return TaskDetailResponse.fromEntity(task);
    }

    @Override
    public TaskDetailResponse getTaskById(Long taskId) {
        AsyncTask task = asyncTaskMapper.selectById(taskId);
        return TaskDetailResponse.fromEntity(task);
    }

    @Override
    public TaskListResponse getTaskList(Long creatorId, String taskType, TaskStatus status,
                                          Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;

        Page<AsyncTask> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<AsyncTask> wrapper = new LambdaQueryWrapper<>();
        
        if (creatorId != null) {
            wrapper.eq(AsyncTask::getCreatorId, creatorId);
        }
        if (taskType != null && !taskType.isBlank()) {
            wrapper.eq(AsyncTask::getTaskType, taskType);
        }
        if (status != null) {
            wrapper.eq(AsyncTask::getStatus, status.getCode());
        }
        
        wrapper.orderByDesc(AsyncTask::getCreateTime);
        
        IPage<AsyncTask> taskPage = asyncTaskMapper.selectPage(page, wrapper);
        
        TaskListResponse response = new TaskListResponse();
        response.setList(taskPage.getRecords().stream()
            .map(TaskDetailResponse::fromEntity)
            .toList());
        response.setTotal(taskPage.getTotal());
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelTask(String taskKey, Long operatorId) {
        AsyncTask task = asyncTaskMapper.selectByTaskKey(taskKey);
        if (task == null) {
            return false;
        }
        
        TaskStatus currentStatus = TaskStatus.fromCode(task.getStatus());
        if (!currentStatus.isCancellable()) {
            throw new RuntimeException("任务当前状态不允许取消: " + currentStatus.getDesc());
        }
        
        AsyncTask updateTask = new AsyncTask();
        updateTask.setStatus(TaskStatus.CANCELLED.getCode());
        updateTask.setEndTime(LocalDateTime.now());
        updateTask.setUpdateTime(LocalDateTime.now());
        
        LambdaQueryWrapper<AsyncTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsyncTask::getTaskKey, taskKey);
        
        int rows = asyncTaskMapper.update(updateTask, wrapper);
        
        // 取消 CompletableFuture
        CompletableFuture<Object> future = taskFutureMap.get(taskKey);
        if (future != null) {
            future.cancel(true);
        }
        
        log.info("取消任务: taskKey={}, operatorId={}", taskKey, operatorId);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTask(String taskKey) {
        AsyncTask task = asyncTaskMapper.selectByTaskKey(taskKey);
        if (task == null) {
            return false;
        }
        
        // 逻辑删除
        AsyncTask updateTask = new AsyncTask();
        updateTask.setDeleted(1);
        updateTask.setUpdateTime(LocalDateTime.now());
        
        LambdaQueryWrapper<AsyncTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsyncTask::getTaskKey, taskKey);
        
        int rows = asyncTaskMapper.update(updateTask, wrapper);
        
        log.info("删除任务: taskKey={}", taskKey);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProgress(String taskKey, Integer progress) {
        AsyncTask updateTask = new AsyncTask();
        updateTask.setProgress(progress);
        updateTask.setUpdateTime(LocalDateTime.now());
        
        LambdaQueryWrapper<AsyncTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsyncTask::getTaskKey, taskKey);
        asyncTaskMapper.update(updateTask, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateResult(String taskKey, Object result) {
        AsyncTask updateTask = new AsyncTask();
        updateTask.setResult(result);
        updateTask.setProgress(100);
        updateTask.setStatus(TaskStatus.SUCCESS.getCode());
        updateTask.setEndTime(LocalDateTime.now());
        updateTask.setUpdateTime(LocalDateTime.now());
        
        LambdaQueryWrapper<AsyncTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsyncTask::getTaskKey, taskKey);
        asyncTaskMapper.update(updateTask, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFailed(String taskKey, String errorMsg) {
        AsyncTask updateTask = new AsyncTask();
        updateTask.setStatus(TaskStatus.FAILED.getCode());
        updateTask.setErrorMsg(errorMsg);
        updateTask.setEndTime(LocalDateTime.now());
        updateTask.setUpdateTime(LocalDateTime.now());
        
        LambdaQueryWrapper<AsyncTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsyncTask::getTaskKey, taskKey);
        asyncTaskMapper.update(updateTask, wrapper);
    }

    private void updateTimeout(String taskKey) {
        AsyncTask updateTask = new AsyncTask();
        updateTask.setStatus(TaskStatus.TIMEOUT.getCode());
        updateTask.setEndTime(LocalDateTime.now());
        updateTask.setUpdateTime(LocalDateTime.now());
        
        LambdaQueryWrapper<AsyncTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsyncTask::getTaskKey, taskKey);
        asyncTaskMapper.update(updateTask, wrapper);
    }

    @Override
    public Object getTaskResult(String taskKey, long timeout) {
        AsyncTask task = asyncTaskMapper.selectByTaskKey(taskKey);
        if (task == null) {
            throw new RuntimeException("任务不存在: " + taskKey);
        }
        
        TaskStatus status = TaskStatus.fromCode(task.getStatus());
        
        if (status == TaskStatus.SUCCESS) {
            return task.getResult();
        }
        if (status == TaskStatus.FAILED) {
            throw new RuntimeException("任务执行失败: " + task.getErrorMsg());
        }
        if (status == TaskStatus.CANCELLED) {
            throw new RuntimeException("任务已取消");
        }
        if (status == TaskStatus.TIMEOUT) {
            throw new RuntimeException("任务已超时");
        }
        
        // 阻塞等待
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeout) {
            task = asyncTaskMapper.selectByTaskKey(taskKey);
            status = TaskStatus.fromCode(task.getStatus());
            
            if (status.isFinished()) {
                if (status == TaskStatus.SUCCESS) {
                    return task.getResult();
                }
                throw new RuntimeException("任务执行失败: " + task.getErrorMsg());
            }
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("等待被中断");
            }
        }
        
        throw new RuntimeException("等待任务结果超时");
    }

    @Override
    public List<TaskDetailResponse> getRecentTasks(Long creatorId, int limit) {
        List<AsyncTask> tasks = asyncTaskMapper.selectByCreatorId(creatorId, limit);
        return tasks.stream()
            .map(TaskDetailResponse::fromEntity)
            .toList();
    }

    /**
     * 获取线程池
     */
    private ThreadPoolTaskExecutor getExecutor(String executorName) {
        if (executorName != null && !executorName.isBlank()) {
            try {
                ThreadPoolTaskExecutor executor = applicationContext.getBean(executorName, ThreadPoolTaskExecutor.class);
                return executor;
            } catch (Exception e) {
                log.warn("未找到线程池 [{}]，使用默认线程池", executorName);
            }
        }
        return defaultExecutor;
    }
}
