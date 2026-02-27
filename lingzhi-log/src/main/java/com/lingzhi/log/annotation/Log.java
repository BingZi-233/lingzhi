package com.lingzhi.log.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 
 * 使用示例：
 * ```java
 * @Log(title = "用户管理", businessType = BusinessType.INSERT)
 * @PostMapping("/user")
 * public Result<Void> addUser() { ... }
 * ```
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 操作模块
     */
    String title() default "";

    /**
     * 业务类型
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 是否保存请求参数
     */
    boolean saveRequestParams() default true;

    /**
     * 是否保存响应参数
     */
    boolean saveResponseParams() default true;

    /**
     * 备注
     */
    String remark() default "";

    /**
     * 业务类型枚举
     */
    enum BusinessType {
        OTHER,
        INSERT,
        UPDATE,
        DELETE,
        GRANT,
        EXPORT,
        IMPORT,
        FORCE,
        CLEAN,
        GEN_CODE
    }
}
