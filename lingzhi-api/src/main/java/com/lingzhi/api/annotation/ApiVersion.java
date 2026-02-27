package com.lingzhi.api.annotation;

import java.lang.annotation.*;

/**
 * API 版本控制注解
 * 
 * 使用示例：
 * ```java
 * @ApiVersion("v2")
 * @GetMapping("/users")
 * public Result<List<User>> listUsers() { ... }
 * ```
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

    /**
     * API 版本
     */
    String value() default "v1";

    /**
     * 版本描述
     */
    String description() default "";
}
