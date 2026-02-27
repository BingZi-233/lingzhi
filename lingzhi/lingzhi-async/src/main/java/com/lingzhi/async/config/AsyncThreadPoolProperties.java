package com.lingzhi.async.config;

import com.lingzhi.async.enums.RejectedPolicyTypeEnum;
import com.lingzhi.async.pool.LingzhiThreadPoolTaskExecutor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * 线程池配置属性
 */
@Data
@ConfigurationProperties(prefix = "lingzhi.async.thread-pool")
public class AsyncThreadPoolProperties {

    /**
     * 是否启用自定义线程池
     */
    private boolean enabled = true;

    /**
     * 核心线程数
     */
    private int corePoolSize = 10;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 50;

    /**
     * 队列容量
     */
    private int queueCapacity = 200;

    /**
     * 线程名前缀
     */
    private String threadNamePrefix = "lingzhi-async-";

    /**
     * 空闲线程存活时间
     */
    private long keepAliveTime = 60;

    /**
     * 空闲线程存活时间单位
     */
    private TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;

    /**
     * 是否允许核心线程超时
     */
    private boolean allowCoreThreadTimeOut = false;

    /**
     * 等待任务在关闭时完成的时间
     */
    private long awaitTerminationSeconds = 60;

    /**
     * 拒绝策略
     */
    private RejectedPolicyTypeEnum rejectedPolicy = RejectedPolicyTypeEnum.CALLER_RUNS;

    /**
     * 是否等待任务完成后再关闭线程池
     */
    private boolean waitForTasksToCompleteOnShutdown = true;
}
