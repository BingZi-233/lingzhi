package com.lingzhi.security.annotation;

import java.lang.annotation.*;

/**
 * 权限注解
 * 
 * 使用示例：
 * ```java
 * @RequiresPermissions("user:add")
 * @PostMapping("/user")
 * public Result<Void> addUser() { ... }
 * 
 * @RequiresRoles("admin")
 * @DeleteMapping("/user/{id}")
 * public Result<Void> deleteUser() { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermissions {

    /**
     * 需要的功能权限
     */
    String[] value() default {};

    /**
     * 需要验证的权限表达式
     */
    String[] permissions() default {};
}
