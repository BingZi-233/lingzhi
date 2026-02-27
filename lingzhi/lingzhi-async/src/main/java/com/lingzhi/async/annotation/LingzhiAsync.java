package com.lingzhi.async.annotation;

import com.lingzhi.async.enums.AsyncExecutorType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务注解
 * 支持指定线程池、超时时间、重试次数等
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LingzhiAsync {

    /**
     * 异步执行器名称
     * 默认使用 lingzhiAsyncExecutor
     */
    String executor() default "lingzhiAsyncExecutor";

    /**
     * 执行器类型
     */
    AsyncExecutorType executorType() default AsyncExecutorType.COMMON;

    /**
     * 任务名称
     */
    String taskName() default "";

    /**
     * 超时时间 (0 表示不超时)
     */
    long timeout() default 0;

    /**
     * 超时时间单位
     */
    TimeUnit timeoutUnit() default TimeUnit.SECONDS;

    /**
     * 任务执行失败后是否重试
     */
    boolean retryable() default false;

    /**
     * 重试次数
     */
    int retryTimes() default 3;

    /**
     * 重试间隔 (毫秒)
     */
    long retryInterval() default 1000;

    /**
     * 任务执行完成回调
     */
    String callback() default "";

    /**
     * 任务执行前等待
     */
    long beforeDelay() default 0;

    /**
     * 任务执行完成等待
     */
    long afterDelay() default 0;

    /**
     * 是否记录日志
     */
    boolean logEnabled() default true;
}
