package com.lingzhi.api.annotation;

import java.lang.annotation.*;

/**
 * API 文档扩展注解
 * 
 * 用于增强 API 文档信息
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiDoc {

    /**
     * 接口标题
     */
    String title() default "";

    /**
     * 接口描述
     */
    String description() default "";

    /**
     * 接口分类
     */
    String category() default "";

    /**
     * 排序
     */
    int order() default 0;

    /**
     * 是否废弃
     */
    boolean deprecated() default false;

    /**
     * 扩展信息（JSON 格式）
     */
    String extensions() default "";
}
