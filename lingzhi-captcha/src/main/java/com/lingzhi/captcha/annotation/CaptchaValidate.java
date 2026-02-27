package com.lingzhi.captcha.annotation;

import java.lang.annotation.*;

/**
 * 验证码校验注解
 * 
 * 使用示例：
 * ```java
 * @CaptchaValidate(key = "#phone")
 * @PostMapping("/sendCode")
 * public Result<Void> sendCode(@RequestParam String phone) { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CaptchaValidate {

    /**
     * 验证码 key，支持 SpEL 表达式
     */
    String key() default "";

    /**
     * 验证码参数名
     */
    String code() default "code";

    /**
     * 是否必需
     */
    boolean required() default true;

    /**
     * 错误消息
     */
    String message() default "验证码错误";
}
