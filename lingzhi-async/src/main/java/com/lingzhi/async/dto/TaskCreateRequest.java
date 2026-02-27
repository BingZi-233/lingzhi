package com.lingzhi.async.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建任务请求
 */
@Data
public class TaskCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务唯一标识（可选，不填则自动生成）
     */
    private String taskKey;

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    private String taskName;

    /**
     * 任务类型/业务类型
     */
    @NotBlank(message = "任务类型不能为空")
    private String taskType;

    /**
     * 任务参数（JSON格式）
     */
    private Object params;

    /**
     * 线程池名称（可选，默认使用默认线程池）
     */
    private String executorName;

    /**
     * 超时时间（毫秒），0或不填表示不超时
     */
    private Long timeout;

    /**
     * 预期执行时间（毫秒），用于估算
     */
    private Long expectedDuration;
}
