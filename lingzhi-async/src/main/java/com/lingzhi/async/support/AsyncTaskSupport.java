package com.lingzhi.async.support;

import com.lingzhi.async.dto.TaskCreateRequest;
import com.lingzhi.async.dto.TaskDetailResponse;
import com.lingzhi.async.entity.AsyncTask;
import com.lingzhi.async.service.AsyncTaskService;
import com.lingzhi.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * 异步任务支持类
 * 
 * 业务 Controller 可以继承此类，获得任务管理能力
 * 
 * 使用示例：
 * ```java
 * @RestController
 * @RequestMapping("/api/export")
 * public class ExportController extends AsyncTaskSupport {
 * 
 *     @PostMapping("/report")
 *     public Result<String> exportReport(@RequestBody ExportRequest request) {
 *         // 方式1: 自动执行
 *         return executeTask("EXPORT_REPORT", "导出报表", request, () -> {
 *             return doExport(request);
 *         });
 *         
 *         // 方式2: 手动控制
 *         String taskKey = createTaskKey();
 *         submitTask(taskKey, "导出报表", "EXPORT_REPORT", request, () -> doExport(request));
 *         return Result.success(taskKey);
 *     }
 *     
 *     @GetMapping("/task/{taskKey}")
 *     public Result<TaskDetailResponse> getTask(@PathVariable String taskKey) {
 *         return Result.success(getTaskDetail(taskKey));
 *     }
 *     
 *     @PostMapping("/task/{taskKey}/cancel")
 *     public Result<Void> cancelTask(@PathVariable String taskKey) {
 *         cancelTask0(taskKey);
 *         return Result.success();
 *     }
 * }
 * ```
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AsyncTaskSupport {

    private final AsyncTaskService asyncTaskService;

    /**
     * 执行异步任务（自动创建任务、执行、更新结果）
     * 
     * @param taskType 任务类型
     * @param taskName 任务名称
     * @param params 任务参数
     * @param callable 任务执行逻辑
     * @return 任务 key
     */
    protected String executeTask(String taskType, String taskName, 
                                 Object params, Callable<Object> callable) {
        return executeTask(taskType, taskName, params, callable, 0L, null);
    }

    /**
     * 执行异步任务（带超时）
     */
    protected String executeTask(String taskType, String taskName,
                                 Object params, Callable<Object> callable,
                                 long timeout) {
        return executeTask(taskType, taskName, params, callable, timeout, null);
    }

    /**
     * 执行异步任务（完整参数）
     */
    protected String executeTask(String taskType, String taskName,
                                 Object params, Callable<Object> callable,
                                 long timeout, String executorName) {
        // 创建任务请求
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTaskType(taskType);
        request.setTaskName(taskName);
        request.setParams(params);
        request.setTimeout(timeout);
        request.setExecutorName(executorName);
        
        // TODO: 从 SecurityContext 获取用户信息
        Long creatorId = 1L;
        String creatorName = "system";
        
        try {
            // 创建并执行任务
            AsyncTask task = asyncTaskService.createAndExecuteTask(request, callable, creatorId, creatorName);
            return task.getTaskKey();
        } catch (Exception e) {
            log.error("执行异步任务失败: taskType={}", taskType, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 提交任务（仅创建任务记录，不立即执行）
     */
    protected String submitTask(String taskType, String taskName, 
                                 Object params) {
        return submitTask(taskType, taskName, params, 0L, null);
    }

    /**
     * 提交任务（完整参数）
     */
    protected String submitTask(String taskType, String taskName,
                                 Object params, long timeout, String executorName) {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTaskType(taskType);
        request.setTaskName(taskName);
        request.setParams(params);
        request.setTimeout(timeout);
        request.setExecutorName(executorName);
        
        Long creatorId = 1L;
        String creatorName = "system";
        
        AsyncTask task = asyncTaskService.createTask(request, creatorId, creatorName);
        return task.getTaskKey();
    }

    /**
     * 开始执行任务
     */
    protected void startTask(String taskKey, Callable<Object> callable) {
        asyncTaskService.executeTask(taskKey, callable);
    }

    /**
     * 开始执行任务（带超时）
     */
    protected Object startTaskWithResult(String taskKey, Callable<Object> callable, long timeout) {
        return asyncTaskService.executeTaskWithResult(taskKey, callable, timeout);
    }

    /**
     * 获取任务详情
     */
    protected TaskDetailResponse getTaskDetail(String taskKey) {
        return asyncTaskService.getTaskDetail(taskKey);
    }

    /**
     * 获取任务详情（根据ID）
     */
    protected TaskDetailResponse getTaskById(Long taskId) {
        return asyncTaskService.getTaskById(taskId);
    }

    /**
     * 取消任务
     */
    protected boolean cancelTask0(String taskKey) {
        return asyncTaskService.cancelTask(taskKey, 1L);
    }

    /**
     * 删除任务
     */
    protected boolean deleteTask0(String taskKey) {
        return asyncTaskService.deleteTask(taskKey);
    }

    /**
     * 获取任务结果（阻塞等待）
     */
    protected Object getTaskResult(String taskKey) {
        return getTaskResult(taskKey, 60000);
    }

    /**
     * 获取任务结果（带超时）
     */
    protected Object getTaskResult(String taskKey, long timeout) {
        return asyncTaskService.getTaskResult(taskKey, timeout);
    }

    /**
     * 更新任务进度
     */
    protected void updateProgress(String taskKey, int progress) {
        asyncTaskService.updateProgress(taskKey, progress);
    }

    /**
     * 更新任务结果
     */
    protected void updateResult(String taskKey, Object result) {
        asyncTaskService.updateResult(taskKey, result);
    }

    /**
     * 更新任务失败
     */
    protected void updateFailed(String taskKey, String errorMsg) {
        asyncTaskService.updateFailed(taskKey, errorMsg);
    }

    /**
     * 生成任务Key（UUID）
     */
    protected String createTaskKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成带前缀的任务Key
     */
    protected String createTaskKey(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" + 
               UUID.randomUUID().toString().substring(0, 8);
    }
}
