package com.lingzhi.mq.annotation;

import java.lang.annotation.*;

/**
 * 消息消费者注解
 * 
 * 使用示例：
 * ```java
 * @MqConsumer(topic = "order-created", tag = "new")
 * public void handleOrderCreated(Message msg) { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqConsumer {

    /**
     * 消息 topic
     */
    String topic() default "";

    /**
     * 消息 tag，* 表示所有
     */
    String tag() default "*";

    /**
     * 消费者组
     */
    String group() default "";

    /**
     * 并发数
     */
    int consumeThreadMin() default 1;

    /**
     * 最大并发数
     */
    int consumeThreadMax() default 1;
}
