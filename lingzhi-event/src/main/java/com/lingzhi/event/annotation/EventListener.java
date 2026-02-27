package com.lingzhi.event.annotation;

import java.lang.annotation.*;

/**
 * 事件监听注解
 * 
 * 使用示例：
 * ```java
 * @EventListener(eventClass = UserRegisterEvent.class)
 * public void handleUserRegister(UserRegisterEvent event) { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListener {

    /**
     * 监听的事件类
     */
    Class<?> eventClass() default Object.class;

    /**
     * 事务阶段：BEFORE_COMMIT, AFTER_COMMIT, AFTER_ROLLBACK
     */
    TransactionPhase phase() default TransactionPhase.AFTER_COMMIT;

    /**
     * 事务阶段枚举
     */
    enum TransactionPhase {
        /**
         * 事务提交前
         */
        BEFORE_COMMIT,
        /**
         * 事务提交后
         */
        AFTER_COMMIT,
        /**
         * 事务回滚后
         */
        AFTER_ROLLBACK
    }
}
