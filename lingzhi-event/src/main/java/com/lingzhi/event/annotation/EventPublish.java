package com.lingzhi.event.annotation;

import java.lang.annotation.*;

/**
 * 事件发布注解
 * 
 * 使用示例：
 * ```java
 * @EventPublish(eventClass = UserRegisterEvent.class)
 * public void register(User user) { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventPublish {

    /**
     * 事件类
     */
    Class<?> eventClass() default Object.class;

    /**
     * 事件源
     */
    String source() default "";

    /**
     * 事件参数，支持 SpEL
     */
    String params() default "";
}
