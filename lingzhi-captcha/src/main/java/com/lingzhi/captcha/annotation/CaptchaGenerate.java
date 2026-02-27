package com.lingzhi.captcha.annotation;

import java.lang.annotation.*;

/**
 * 生成验证码注解
 * 
 * 使用示例：
 * ```java
 * @CaptchaGenerate(key = "#request.phone")
 * @GetMapping("/captcha")
 * public Result<CaptchaResult> generate(@RequestBody CaptchaRequest request) { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CaptchaGenerate {

    /**
     * 验证码 key，支持 SpEL 表达式
     */
    String key() default "";

    /**
     * 验证码类型：image/sms/email
     */
    String type() default "image";

    /**
     * 过期时间（秒）
     */
    long expire() default 300;

    /**
     * 长度
     */
    int length() default 4;
}
