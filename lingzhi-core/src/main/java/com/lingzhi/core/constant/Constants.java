package com.lingzhi.core.constant;

/**
 * 全局常量
 */
public final class Constants {

    private Constants() {}

    /**
     * 成功响应码
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 失败响应码
     */
    public static final int ERROR_CODE = 500;

    /**
     * 未授权
     */
    public static final int UNAUTHORIZED_CODE = 401;

    /**
     * 禁止访问
     */
    public static final int FORBIDDEN_CODE = 403;

    /**
     * 资源不存在
     */
    public static final int NOT_FOUND_CODE = 404;

    /**
     * 请求参数错误
     */
    public static final int BAD_REQUEST_CODE = 400;

    /**
     * 业务异常
     */
    public static final int BUSINESS_ERROR_CODE = 1000;

    /**
     * 成功消息
     */
    public static final String SUCCESS_MSG = "操作成功";

    /**
     * 失败消息
     */
    public static final String ERROR_MSG = "操作失败";

    /**
     * 当前请求的线程变量名
     */
    public static final String REQUEST_ID = "request_id";

    /**
     * 当前用户线程变量名
     */
    public static final String CURRENT_USER = "current_user";

    /**
     * 当前用户ID线程变量名
     */
    public static final String CURRENT_USER_ID = "current_user_id";

    /**
     * 默认分页页码
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大分页大小
     */
    public static final int MAX_PAGE_SIZE = 1000;

    /**
     * 日期时间格式
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 时间格式
     */
    public static final String TIME_PATTERN = "HH:mm:ss";
}
