package com.lingzhi.scheduler.config;

import com.lingzhi.scheduler.annotation.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.concurrent.Executors;

/**
 * 定时任务配置
 */
@Slf4j
@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "lingzhi.scheduler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SchedulerConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 设置线程池
        taskRegistrar.setTaskScheduler(new ConcurrentTaskScheduler(
            Executors.newScheduledThreadPool(10)
        ));
        
        log.info("定时任务配置已加载");
    }
}
