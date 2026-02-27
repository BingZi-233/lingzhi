package com.lingzhi.monitor.annotation;

import java.lang.annotation.*;

/**
 * 数据脱敏注解
 * 
 * 使用示例：
 * ```java
 * @Sensitive(SensitiveType.PHONE)
 * private String phone;
 * ```
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {

    /**
     * 脱敏类型
     */
    SensitiveType type() default SensitiveType.NONE;

    /**
     * 自定义前缀长度
     */
    int prefixLen() default 0;

    /**
     * 自定义后缀长度
     */
    int suffixLen() default 0;

    /**
     * 脱敏类型枚举
     */
    enum SensitiveType {
        /**
         * 不脱敏
         */
        NONE,
        /**
         * 姓名：张*丰
         */
        NAME,
        /**
         * 手机号：138****1234
         */
        PHONE,
        /**
         * 邮箱：a***@example.com
         */
        EMAIL,
        /**
         * 身份证：3201***********1234
         */
        ID_CARD,
        /**
         * 银行卡：6228***********1234
         */
        BANK_CARD,
        /**
         * 地址：江苏省南京市***
         */
        ADDRESS
    }
}
