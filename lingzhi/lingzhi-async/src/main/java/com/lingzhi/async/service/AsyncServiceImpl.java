package com.lingzhi.async.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 异步任务服务实现
 */
@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {

    @Qualifier("lingzhiAsyncExecutor")
    private final ThreadPoolTaskExecutor defaultExecutor;

    private final ConcurrentHashMap<String, ThreadPoolTaskExecutor> executorMap = new ConcurrentHashMap<>();

    public AsyncServiceImpl(@Qualifier("lingzhiAsyncExecutor") ThreadPoolTaskExecutor defaultExecutor) {
        this.defaultExecutor = defaultExecutor;
        this.executorMap.put("lingzhiAsyncExecutor", defaultExecutor);
    }

    /**
     * 注册线程池
     */
    public void registerExecutor(String name, ThreadPoolTaskExecutor executor) {
        executorMap.put(name, executor);
    }

    @Override
    public void execute(Runnable task) {
        execute(task, "lingzhiAsyncExecutor");
    }

    @Override
    public void execute(Runnable task, String executorName) {
        ThreadPoolTaskExecutor executor = getExecutor(executorName);
        executor.execute(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return submit(task, "lingzhiAsyncExecutor");
    }

    @Override
    public <T> Future<T> submit(Callable<T> task, String executorName) {
        ThreadPoolTaskExecutor executor = getExecutor(executorName);
        return executor.submit(task);
    }

    @Override
    public <T> T submitAndWait(Callable<T> task, long timeout, TimeUnit unit) {
        return submitAndWait(task, "lingzhiAsyncExecutor", timeout, unit);
    }

    @Override
    public <T> T submitAndWait(Callable<T> task, String executorName, long timeout, TimeUnit unit) {
        ThreadPoolTaskExecutor executor = getExecutor(executorName);
        Future<T> future = executor.submit(task);
        try {
            return future.get(timeout, unit);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new RuntimeException("异步任务执行超时", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("异步任务执行被中断", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("异步任务执行失败", e);
        }
    }

    @Override
    public <T> List<Future<T>> submitAll(Collection<? extends Callable<T>> tasks) {
        return submitAll(tasks, "lingzhiAsyncExecutor");
    }

    @Override
    public <T> List<Future<T>> submitAll(Collection<? extends Callable<T>> tasks, String executorName) {
        ThreadPoolTaskExecutor executor = getExecutor(executorName);
        return tasks.stream().map(executor::submit).toList();
    }

    @Override
    public void executeWithCallback(Runnable task, Runnable successCallback, Runnable failureCallback) {
        executeWithCallback(task, "lingzhiAsyncExecutor", successCallback, failureCallback);
    }

    @Override
    public void executeWithCallback(Runnable task, String executorName,
                                     Runnable successCallback, Runnable failureCallback) {
        ThreadPoolTaskExecutor executor = getExecutor(executorName);
        executor.execute(() -> {
            try {
                task.run();
                if (successCallback != null) {
                    successCallback.run();
                }
            } catch (Exception e) {
                if (failureCallback != null) {
                    failureCallback.run();
                }
                throw e;
            }
        });
    }

    @Override
    public <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return supplyAsync(supplier, "lingzhiAsyncExecutor");
    }

    @Override
    public <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier, String executorName) {
        ThreadPoolTaskExecutor executor = getExecutor(executorName);
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    @Override
    public CompletableFuture<Void> runAsync(Runnable runnable) {
        return runAsync(runnable, "lingzhiAsyncExecutor");
    }

    @Override
    public CompletableFuture<Void> runAsync(Runnable runnable, String executorName) {
        ThreadPoolTaskExecutor executor = getExecutor(executorName);
        return CompletableFuture.runAsync(runnable, executor);
    }

    @Override
    public String getExecutorInfo(String executorName) {
        ThreadPoolTaskExecutor taskExecutor = getExecutor(executorName);
        ThreadPoolExecutor executor = taskExecutor.getThreadPoolExecutor();
        return String.format("[%s] 活跃: %d, 完成: %d, 任务: %d, 队列: %d",
                executorName,
                executor.getActiveCount(),
                executor.getCompletedTaskCount(),
                executor.getTaskCount(),
                executor.getQueue().size());
    }

    @Override
    public Executor getDefaultExecutor() {
        return defaultExecutor;
    }

    /**
     * 获取线程池
     */
    private ThreadPoolTaskExecutor getExecutor(String executorName) {
        return executorMap.computeIfAbsent(executorName, name -> {
            log.warn("未找到线程池 [{}]，使用默认线程池", name);
            return defaultExecutor;
        });
    }
}
