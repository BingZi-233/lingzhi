package com.lingzhi.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    /**
     * 正常
     */
    NORMAL(0, "正常"),

    /**
     * 禁用
     */
    DISABLED(1, "禁用"),

    /**
     * 删除
     */
    DELETED(2, "已删除");

    private final int code;
    private final String desc;

    /**
     * 根据 code 获取枚举
     */
    public static StatusEnum of(Integer code) {
        if (code == null) {
            return null;
        }
        for (StatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断是否为正常状态
     */
    public boolean isNormal() {
        return this == NORMAL;
    }

    /**
     * 判断是否为禁用状态
     */
    public boolean isDisabled() {
        return this == DISABLED;
    }

    /**
     * 判断是否为已删除状态
     */
    public boolean isDeleted() {
        return this == DELETED;
    }
}
