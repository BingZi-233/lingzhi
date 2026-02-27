package com.lingzhi.mq.annotation;

import java.lang.annotation.*;

/**
 * 消息生产者注解
 * 
 * 使用示例：
 * ```java
 * @MqProducer(topic = "order-created", tag = "new")
 * public void sendOrderCreated(Order order) { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqProducer {

    /**
     * 消息 topic
     */
    String topic() default "";

    /**
     * 消息 tag
     */
    String tag() default "";

    /**
     * 消息 keys
     */
    String keys() default "";

    /**
     * 延迟等级：1-18
     * 1: 1秒 2: 5秒 3: 10秒 4: 30秒 5: 1分钟 ...
     */
    int delayLevel() default 0;
}
