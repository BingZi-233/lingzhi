package com.lingzhi.api.annotation;

import java.lang.annotation.*;

/**
 * 忽略统一响应包装
 * 
 * 使用示例：
 * ```java
 * @IgnoreResponse
 * @GetMapping("/health")
 * public Health health() { return Health.up(); }
 * ```
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreResponse {
}
