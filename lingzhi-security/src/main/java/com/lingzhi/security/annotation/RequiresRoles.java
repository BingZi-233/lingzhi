package com.lingzhi.security.annotation;

import java.lang.annotation.*;

/**
 * 角色注解
 * 
 * 使用示例：
 * ```java
 * @RequiresRoles("admin")
 * @GetMapping("/admin")
 * public Result<Void> admin() { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresRoles {

    /**
     * 需要验证的角色
     */
    String[] value() default {};

    /**
     * 角色验证模式：ALL 必须全部拥有, ANY 拥有其一即可
     */
    Mode mode() default Mode.ALL;

    /**
     * 角色验证模式
     */
    enum Mode {
        /**
         * 必须全部拥有
         */
        ALL,
        /**
         * 拥有其一即可
         */
        ANY
    }
}
