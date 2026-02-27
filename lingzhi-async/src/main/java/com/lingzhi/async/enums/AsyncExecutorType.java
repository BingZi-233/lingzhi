package com.lingzhi.async.enums;

/**
 * 异步执行器类型枚举
 */
public enum AsyncExecutorType {

    /**
     * 通用执行器
     */
    COMMON("common", "通用执行器"),

    /**
     * IO密集型执行器
     */
    IO("io", "IO密集型执行器"),

    /**
     * CPU密集型执行器
     */
    CPU("cpu", "CPU密集型执行器"),

    /**
     * 快速执行器
     */
    FAST("fast", "快速执行器"),

    /**
     * 慢速执行器
     */
    SLOW("slow", "慢速执行器"),

    /**
     * 邮件发送执行器
     */
    MAIL("mail", "邮件发送执行器"),

    /**
     * 消息推送执行器
     */
    PUSH("push", "消息推送执行器"),

    /**
     * 文件处理执行器
     */
    FILE("file", "文件处理执行器");

    private final String code;
    private final String desc;

    AsyncExecutorType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据 code 获取枚举
     */
    public static AsyncExecutorType of(String code) {
        for (AsyncExecutorType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return COMMON;
    }
}
