package com.lingzhi.async.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 异步任务实体
 */
@Data
@TableName(value = "async_task", autoResultMap = true)
public class AsyncTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务唯一标识（用于查询）
     */
    private String taskKey;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型/业务类型
     */
    private String taskType;

    /**
     * 任务参数（JSON格式）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Object params;

    /**
     * 任务结果（JSON格式）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Object result;

    /**
     * 任务状态：0-待执行 1-执行中 2-执行成功 3-执行失败 4-已取消 5-已超时
     */
    private Integer status;

    /**
     * 失败原因
     */
    private String errorMsg;

    /**
     * 进度百分比（0-100）
     */
    private Integer progress;

    /**
     * 线程池名称
     */
    private String executorName;

    /**
     * 预期执行时间（毫秒）
     */
    private Long expectedDuration;

    /**
     * 实际执行时间（毫秒）
     */
    private Long actualDuration;

    /**
     * 超时时间（毫秒），0表示不超时
     */
    private Long timeout;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建者名称
     */
    private String creatorName;

    /**
     * 执行开始时间
     */
    private LocalDateTime startTime;

    /**
     * 执行结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer deleted;
}
