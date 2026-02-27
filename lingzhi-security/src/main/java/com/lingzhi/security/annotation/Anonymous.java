package com.lingzhi.security.annotation;

import java.lang.annotation.*;

/**
 * 匿名访问注解
 * 
 * 使用示例：
 * ```java
 * @Anonymous
 * @PostMapping("/login")
 * public Result<LoginResponse> login() { ... }
 * ```
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Anonymous {
}
