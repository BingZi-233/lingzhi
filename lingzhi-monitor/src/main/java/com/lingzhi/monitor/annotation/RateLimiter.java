package com.lingzhi.monitor.annotation;

import java.lang.annotation.*;

/**
 * 限流注解
 * 
 * 使用示例：
 * ```java
 * @RateLimiter(key = "api:user:list", permitsPerSecond = 10)
 * @GetMapping("/users")
 * public Result<List<User>> listUsers() { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    /**
     * 限流 key
     */
    String key() default "";

    /**
     * 每秒允许的请求数
     */
    double permitsPerSecond() default 10;

    /**
     * 获取令牌超时时间（毫秒）
     */
    long timeout() default 0;

    /**
     * 限流提示消息
     */
    String message() default "请求过于频繁，请稍后重试";
}
