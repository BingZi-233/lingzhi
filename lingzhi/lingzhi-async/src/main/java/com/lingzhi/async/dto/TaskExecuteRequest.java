package com.lingzhi.async.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 任务执行请求（用于提交任务执行逻辑）
 */
@Data
public class TaskExecuteRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务唯一标识
     */
    @NotBlank(message = "任务标识不能为空")
    private String taskKey;

    /**
     * 业务处理器的Bean名称
     */
    @NotBlank(message = "处理器名称不能为空")
    private String handlerBeanName;

    /**
     * 回调方法名（默认 execute）
     */
    private String methodName = "execute";
}
