package com.lingzhi.async.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 异步任务服务接口
 */
public interface AsyncService {

    /**
     * 异步执行无返回值的任务
     */
    void execute(Runnable task);

    /**
     * 异步执行无返回值的任务（指定线程池）
     */
    void execute(Runnable task, String executorName);

    /**
     * 异步执行有返回值的任务
     */
    <T> Future<T> submit(Callable<T> task);

    /**
     * 异步执行有返回值的任务（指定线程池）
     */
    <T> Future<T> submit(Callable<T> task, String executorName);

    /**
     * 异步执行任务并获取结果（带超时）
     */
    <T> T submitAndWait(Callable<T> task, long timeout, TimeUnit unit);

    /**
     * 异步执行任务并获取结果（指定线程池和超时）
     */
    <T> T submitAndWait(Callable<T> task, String executorName, long timeout, TimeUnit unit);

    /**
     * 批量异步执行任务
     */
    <T> List<Future<T>> submitAll(Collection<? extends Callable<T>> tasks);

    /**
     * 批量异步执行任务（指定线程池）
     */
    <T> List<Future<T>> submitAll(Collection<? extends Callable<T>> tasks, String executorName);

    /**
     * 异步执行任务（带回调）
     */
    void executeWithCallback(Runnable task, Runnable successCallback, Runnable failureCallback);

    /**
     * 异步执行任务（带回调，指定线程池）
     */
    void executeWithCallback(Runnable task, String executorName, Runnable successCallback, Runnable failureCallback);

    /**
     * 使用 CompletableFuture 异步执行
     */
    <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier);

    /**
     * 使用 CompletableFuture 异步执行（指定线程池）
     */
    <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier, String executorName);

    /**
     * 使用 CompletableFuture 异步执行无返回值的任务
     */
    CompletableFuture<Void> runAsync(Runnable runnable);

    /**
     * 使用 CompletableFuture 异步执行无返回值的任务（指定线程池）
     */
    CompletableFuture<Void> runAsync(Runnable runnable, String executorName);

    /**
     * 获取线程池信息
     */
    String getExecutorInfo(String executorName);

    /**
     * 获取默认线程池
     */
    Executor getDefaultExecutor();
}
