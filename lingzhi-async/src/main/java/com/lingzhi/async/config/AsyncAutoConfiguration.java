package com.lingzhi.async.config;

import com.lingzhi.async.annotation.LingzhiAsync;
import com.lingzhi.async.enums.RejectedPolicyTypeEnum;
import com.lingzhi.async.mapper.AsyncTaskMapper;
import com.lingzhi.async.pool.LingzhiThreadPoolTaskExecutor;
import com.lingzhi.async.service.AsyncService;
import com.lingzhi.async.service.AsyncServiceImpl;
import com.lingzhi.async.service.AsyncTaskService;
import com.lingzhi.async.service.AsyncTaskServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务自动配置
 */
@Slf4j
@AutoConfiguration
@EnableAsync
@EnableConfigurationProperties(AsyncThreadPoolProperties.class)
@MapperScan("com.lingzhi.async.mapper")
@ConditionalOnProperty(prefix = "lingzhi.async", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AsyncAutoConfiguration {

    public AsyncAutoConfiguration() {
        log.info("灵芝异步任务模块启动 - Lingzhi Async Starter");
    }

    /**
     * 默认异步线程池
     */
    @Bean(name = "lingzhiAsyncExecutor")
    @ConditionalOnMissingBean(name = "lingzhiAsyncExecutor")
    public Executor lingzhiAsyncExecutor(AsyncThreadPoolProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 基本配置
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        executor.setKeepAliveSeconds((int) properties.getKeepAliveTime());
        executor.setAllowCoreThreadTimeOut(properties.isAllowCoreThreadTimeOut());

        // 拒绝策略
        executor.setRejectedExecutionHandler(createRejectedExecutionHandler(properties.getRejectedPolicy()));

        // 等待任务完成
        executor.setWaitForTasksToCompleteOnShutdown(properties.isWaitForTasksToCompleteOnShutdown());
        executor.setAwaitTerminationSeconds((int) properties.getAwaitTerminationSeconds());

        // 初始化
        executor.initialize();

        log.info("灵芝异步线程池初始化完成 - {}", getPoolInfo(executor));
        return executor;
    }

    /**
     * 创建拒绝策略处理器
     */
    private RejectedExecutionHandler createRejectedExecutionHandler(RejectedPolicyTypeEnum policy) {
        return switch (policy) {
            case CALLER_RUNS -> new ThreadPoolExecutor.CallerRunsPolicy();
            case ABORT_POLICY -> new ThreadPoolExecutor.AbortPolicy();
            case DISCARD_OLDEST_POLICY -> new ThreadPoolExecutor.DiscardOldestPolicy();
            case DISCARD_POLICY -> new ThreadPoolExecutor.DiscardPolicy();
            case LOG_DISCARD_POLICY -> (r, e) -> {
                log.warn("线程池拒绝任务: {}", r);
            };
        };
    }

    /**
     * 获取线程池信息
     */
    private String getPoolInfo(ThreadPoolTaskExecutor executor) {
        return String.format("核心: %d, 最大: %d, 队列: %d",
                executor.getCorePoolSize(),
                executor.getMaxPoolSize(),
                executor.getQueueCapacity());
    }

    /**
     * 异步服务
     */
    @Bean
    @ConditionalOnMissingBean
    public AsyncService asyncService(ThreadPoolTaskExecutor lingzhiAsyncExecutor) {
        return new AsyncServiceImpl(lingzhiAsyncExecutor);
    }

    /**
     * 异步任务管理服务
     */
    @Bean
    @ConditionalOnMissingBean
    public AsyncTaskService asyncTaskService(AsyncTaskMapper asyncTaskMapper, 
                                              ApplicationContext applicationContext,
                                              ThreadPoolTaskExecutor lingzhiAsyncExecutor) {
        AsyncTaskServiceImpl service = new AsyncTaskServiceImpl(asyncTaskMapper, applicationContext);
        service.setDefaultExecutor(lingzhiAsyncExecutor);
        return service;
    }
}
