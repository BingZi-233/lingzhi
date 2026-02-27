package com.lingzhi.cache.annotation;

import java.lang.annotation.*;

/**
 * 缓存清除注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheEvict {

    /**
     * 缓存 key，支持 SpEL 表达式
     */
    String key() default "";

    /**
     * 缓存名称
     */
    String value() default "";

    /**
     * 是否清除所有缓存
     */
    boolean allEntries() default false;
}
