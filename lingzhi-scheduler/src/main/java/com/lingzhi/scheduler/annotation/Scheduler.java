package com.lingzhi.scheduler.annotation;

import java.lang.annotation.*;

/**
 * 定时任务注解
 * 
 * 使用示例：
 * ```java
 * @Scheduler(cron = "0 0 2 * * ?", description = "清理缓存")
 * public void cleanCache() { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scheduler {

    /**
     * Cron 表达式
     */
    String cron() default "";

    /**
     * 固定频率（毫秒）
     */
    long fixedRate() default -1;

    /**
     * 固定延迟（毫秒）
     */
    long fixedDelay() default -1;

    /**
     * 初始延迟（毫秒）
     */
    long initialDelay() default -1;

    /**
     * 任务描述
     */
    String description() default "";

    /**
     * 任务分组
     */
    String group() default "default";
}
