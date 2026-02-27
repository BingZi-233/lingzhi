package com.lingzhi.async.enums;

/**
 * 拒绝策略类型枚举
 */
public enum RejectedPolicyTypeEnum {

    /**
     * 由调用线程执行
     */
    CALLER_RUNS("CallerRunsPolicy", "由调用线程执行"),

    /**
     * 丢弃队列中最新的任务
     */
    ABORT_POLICY("AbortPolicy", "丢弃队列中最新的任务并抛出异常"),

    /**
     * 丢弃队列中最旧的任务
     */
    DISCARD_OLDEST_POLICY("DiscardOldestPolicy", "丢弃队列中最旧的任务"),

    /**
     * 直接丢弃任务
     */
    DISCARD_POLICY("DiscardPolicy", "直接丢弃任务"),

    /**
     * 自定义拒绝策略（记录日志并丢弃）
     */
    LOG_DISCARD_POLICY("LogDiscardPolicy", "记录日志并丢弃任务");

    private final String name;
    private final String desc;

    RejectedPolicyTypeEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
