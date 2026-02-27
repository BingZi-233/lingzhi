package com.lingzhi.monitor.annotation;

import java.lang.annotation.*;

/**
 * 接口幂等注解
 * 
 * 使用示例：
 * ```java
 * @Idempotent(key = "'order:' + #request.orderNo", expire = 30)
 * @PostMapping("/createOrder")
 * public Result<Void> createOrder(@RequestBody OrderRequest request) { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    /**
     * 幂等 key，支持 SpEL 表达式
     */
    String key() default "";

    /**
     * 过期时间（秒）
     */
    int expire() default 60;

    /**
     * 错误消息
     */
    String message() default "请求重复，请稍后重试";
}
