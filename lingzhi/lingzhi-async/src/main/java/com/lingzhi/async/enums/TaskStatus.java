package com.lingzhi.async.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举
 */
@Getter
@AllArgsConstructor
public enum TaskStatus {

    /**
     * 待执行
     */
    PENDING(0, "待执行"),

    /**
     * 执行中
     */
    RUNNING(1, "执行中"),

    /**
     * 执行成功
     */
    SUCCESS(2, "执行成功"),

    /**
     * 执行失败
     */
    FAILED(3, "执行失败"),

    /**
     * 已取消
     */
    CANCELLED(4, "已取消"),

    /**
     * 已超时
     */
    TIMEOUT(5, "已超时");

    @EnumValue
    @JsonValue
    private final int code;

    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static TaskStatus fromCode(int code) {
        for (TaskStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }

    /**
     * 是否为终止状态（成功/失败/取消/超时）
     */
    public boolean isFinished() {
        return this == SUCCESS || this == FAILED || this == CANCELLED || this == TIMEOUT;
    }

    /**
     * 是否可以取消
     */
    public boolean isCancellable() {
        return this == PENDING || this == RUNNING;
    }
}
