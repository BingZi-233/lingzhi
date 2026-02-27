package com.lingzhi.async.aspect;

import com.lingzhi.async.annotation.LingzhiAsyncTask;
import com.lingzhi.async.dto.TaskCreateRequest;
import com.lingzhi.async.entity.AsyncTask;
import com.lingzhi.async.service.AsyncTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 异步任务注解切面
 * 
 * 拦截带有 @AsyncTask 注解的方法，实现：
 * 1. 自动创建任务
 * 2. 在后台线程执行方法
 * 3. 立即返回 taskKey
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AsyncTaskAspect {

    private final AsyncTaskService asyncTaskService;
    private final ApplicationContext applicationContext;
    
    private final ExpressionParser parser = new SpelExpressionParser();
    
    // 任务结果缓存（用于阻塞获取结果）
    private final ConcurrentHashMap<String, CompletableFuture<Object>> resultCache = new ConcurrentHashMap<>();

    /**
     * 环绕通知：拦截 @AsyncTask 注解的方法
     */
    @Around("@annotation(com.lingzhi.async.annotation.LingzhiAsyncTask)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        LingzhiAsyncTask asyncTask = method.getAnnotation(LingzhiAsyncTask.class);
        
        // 1. 生成 taskKey
        String taskKey = generateTaskKey(asyncTask, joinPoint);
        
        // 2. 获取任务参数
        Object params = extractParams(asyncTask, joinPoint);
        
        // 3. 获取任务名称
        String taskName = resolveTaskName(asyncTask, joinPoint);
        
        // 4. 创建任务记录
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTaskKey(taskKey);
        request.setTaskName(taskName);
        request.setTaskType(asyncTask.taskType());
        request.setParams(params);
        request.setTimeout(asyncTask.timeout());
        request.setExpectedDuration(asyncTask.expectedDuration());
        request.setExecutorName(asyncTask.executorName());
        
        // 获取当前用户（TODO: 从 SecurityContext 获取）
        Long creatorId = 1L;
        String creatorName = "system";
        
        // 5. 创建任务
        AsyncTask task = asyncTaskService.createTask(request, creatorId, creatorName);
        
        // 6. 异步执行方法
        executeAsync(taskKey, joinPoint, asyncTask);
        
        // 7. 返回 taskKey
        return taskKey;
    }

    /**
     * 生成 taskKey
     */
    private String generateTaskKey(LingzhiAsyncTask asyncTask, ProceedingJoinPoint joinPoint) {
        String prefix = asyncTask.taskType().toLowerCase();
        return prefix + "_" + System.currentTimeMillis() + "_" + 
               UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 提取任务参数
     */
    private Object extractParams(LingzhiAsyncTask asyncTask, ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 1) {
            return args[0];
        }
        return args;
    }

    /**
     * 解析任务名称
     */
    private String resolveTaskName(LingzhiAsyncTask asyncTask, ProceedingJoinPoint joinPoint) {
        String taskName = asyncTask.taskName();
        
        if (taskName == null || taskName.isEmpty()) {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            taskName = method.getName();
        } else if (taskName.contains("#")) {
            try {
                StandardEvaluationContext context = new StandardEvaluationContext();
                String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
                if (paramNames != null) {
                    for (int i = 0; i < paramNames.length; i++) {
                        context.setVariable(paramNames[i], joinPoint.getArgs()[i]);
                    }
                }
                Object value = parser.parseExpression(taskName).getValue(context);
                taskName = value != null ? value.toString() : taskName;
            } catch (Exception e) {
                log.warn("解析任务名称 SpEL 失败: {}", taskName, e);
            }
        }
        
        return taskName;
    }

    /**
     * 异步执行方法
     */
    private void executeAsync(String taskKey, ProceedingJoinPoint joinPoint, LingzhiAsyncTask asyncTask) {
        ThreadPoolTaskExecutor executor = getExecutor(asyncTask.executorName());
        
        CompletableFuture<Object> future = new CompletableFuture<>();
        resultCache.put(taskKey, future);
        
        executor.execute(() -> {
            long startTime = System.currentTimeMillis();
            try {
                asyncTaskService.updateProgress(taskKey, 0);
                
                Object result = joinPoint.proceed();
                
                asyncTaskService.updateResult(taskKey, result);
                future.complete(result);
                
                long duration = System.currentTimeMillis() - startTime;
                log.info("异步任务执行成功: taskKey={}, duration={}ms", taskKey, duration);
                
            } catch (Throwable e) {
                asyncTaskService.updateFailed(taskKey, e.getMessage());
                future.completeExceptionally(e);
                log.error("异步任务执行失败: taskKey={}, error={}", taskKey, e.getMessage(), e);
            }
        });
        
        // 超时检查
        if (asyncTask.timeout() > 0) {
            executor.execute(() -> {
                try {
                    Thread.sleep(asyncTask.timeout());
                    var task = asyncTaskService.getTaskDetail(taskKey);
                    if (task != null && "RUNNING".equals(task.getStatus().name())) {
                        asyncTaskService.updateFailed(taskKey, "任务执行超时");
                        CompletableFuture<Object> f = resultCache.get(taskKey);
                        if (f != null) {
                            f.completeExceptionally(new TimeoutException("任务执行超时"));
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    /**
     * 获取线程池
     */
    private ThreadPoolTaskExecutor getExecutor(String executorName) {
        if (executorName != null && !executorName.isBlank()) {
            try {
                return applicationContext.getBean(executorName, ThreadPoolTaskExecutor.class);
            } catch (Exception e) {
                log.warn("未找到线程池 [{}]，使用默认线程池", executorName);
            }
        }
        return applicationContext.getBean("lingzhiAsyncExecutor", ThreadPoolTaskExecutor.class);
    }

    /**
     * 获取任务结果（阻塞等待）
     */
    public Object getTaskResult(String taskKey, long timeout) {
        CompletableFuture<Object> cached = resultCache.get(taskKey);
        if (cached != null) {
            try {
                return cached.get(timeout, TimeUnit.MILLISECONDS);
            } catch (TimeoutException | InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return asyncTaskService.getTaskResult(taskKey, timeout);
    }
}
