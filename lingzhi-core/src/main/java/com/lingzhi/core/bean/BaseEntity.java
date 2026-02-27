package com.lingzhi.core.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 基础实体
 */
@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 创建者ID
     */
    private Long createBy;

    /**
     * 更新者ID
     */
    private Long updateBy;

    /**
     * 备注
     */
    private String remark;
}
