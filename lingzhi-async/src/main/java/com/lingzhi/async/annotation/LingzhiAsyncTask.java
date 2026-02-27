package com.lingzhi.async.annotation;

import java.lang.annotation.*;

/**
 * 异步任务注解
 * 
 * 标注在 Controller 方法上，自动将同步方法改为异步执行
 * 
 * 使用示例：
 * ```java
 * @LingzhiAsyncTask(taskType = "EXPORT_REPORT", taskName = "导出报表")
 * @PostMapping("/export")
 * public Result<String> exportReport(@RequestBody ExportRequest request) {
 *     // 执行耗时操作，返回结果会自动存储
 *     return Result.success(doExport(request));
 * }
 * ```
 * 
 * 效果：
 * - 接口立即返回 taskKey
 * - 方法在后台线程执行
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LingzhiAsyncTask {

    /**
     * 任务类型（必填）
     * 用于区分不同类型的任务
     */
    String taskType();

    /**
     * 任务名称
     * 支持 SpEL 表达式，如：#request.reportType
     */
    String taskName() default "";

    /**
     * 超时时间（毫秒）
     * 0 或不填表示不超时
     */
    long timeout() default 0;

    /**
     * 预期执行时间（毫秒），用于进度估算
     */
    long expectedDuration() default 0;

    /**
     * 线程池名称
     * 不填使用默认线程池
     */
    String executorName() default "";

    /**
     * 任务参数来源
     * SpEL 表达式，默认为方法参数
     */
    String params() default "";

    /**
     * 是否自动生成任务管理端点
     * true: 自动生成 /task/{taskKey} 系列端点
     */
    boolean enableManagement() default true;

    /**
     * 任务管理端点前缀
     * 默认为 /task
     */
    String managementPath() default "/task";
}
