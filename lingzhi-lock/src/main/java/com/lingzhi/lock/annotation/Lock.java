package com.lingzhi.lock.annotation;

import java.lang.annotation.*;

/**
 * 分布式锁注解
 * 
 * 使用示例：
 * ```java
 * @Lock(keys = "'order:' + #orderId")
 * public void processOrder(Long orderId) { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

    /**
     * 锁的 key，支持 SpEL 表达式
     */
    String key() default "";

    /**
     * 锁的前缀
     */
    String prefix() default "lock";

    /**
     * 等待时间（秒），0 表示立即返回
     */
    long waitTime() default 0;

    /**
     * 持有锁时间（秒），-1 表示永久
     */
    long leaseTime() default 30;
}
