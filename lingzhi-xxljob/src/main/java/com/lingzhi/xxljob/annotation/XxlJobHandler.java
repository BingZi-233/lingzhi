package com.lingzhi.xxljob.annotation;

import java.lang.annotation.*;

/**
 * XXL-Job 任务注解
 * 
 * 使用示例：
 * ```java
 * @XxlJobHandler("demoJob")
 * public ReturnT<String> execute(String params) {
 *     return ReturnT.SUCCESS;
 * }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XxlJobHandler {

    /**
     * 任务名称
     */
    String value() default "";

    /**
     * 任务描述
     */
    String description() default "";

    /**
     * 初始化方法
     */
    String init() default "";

    /**
     * 销毁方法
     */
    String destroy() default "";
}
