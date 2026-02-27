package com.lingzhi.web.annotation;

import java.lang.annotation.*;

/**
 * 租户标识注解
 * 
 * 使用示例：
 * ```java
 * @Tenant
 * @GetMapping("/users")
 * public Result<List<User>> listUsers() { ... }
 * ```
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Tenant {

    /**
     * 是否忽略租户过滤
     */
    boolean ignore() default false;

    /**
     * 租户 ID 来源，默认从请求头 X-Tenant-Id 获取
     */
    String source() default "header";
}
