package com.lingzhi.cache.annotation;

import java.lang.annotation.*;

/**
 * 缓存注解
 * 
 * 使用示例：
 * ```java
 * @Cachable(key = "'user:' + #id")
 * public User getUser(Long id) { ... }
 * 
 * @CacheEvict(key = "'user:' + #id")
 * public void deleteUser(Long id) { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cachable {

    /**
     * 缓存 key，支持 SpEL 表达式
     */
    String key() default "";

    /**
     * 缓存名称
     */
    String value() default "";

    /**
     * 过期时间（秒）
     */
    long expire() default -1;

    /**
     * 是否清除所有缓存
     */
    boolean allEntries() default false;
}
