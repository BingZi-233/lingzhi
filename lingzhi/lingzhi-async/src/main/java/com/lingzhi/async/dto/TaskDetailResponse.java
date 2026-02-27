package com.lingzhi.async.dto;

import com.lingzhi.async.entity.AsyncTask;
import com.lingzhi.async.enums.TaskStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务详情响应
 */
@Data
public class TaskDetailResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private Long id;

    /**
     * 任务唯一标识
     */
    private String taskKey;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 任务参数
     */
    private Object params;

    /**
     * 任务结果
     */
    private Object result;

    /**
     * 任务状态
     */
    private TaskStatus status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 失败原因
     */
    private String errorMsg;

    /**
     * 进度百分比
     */
    private Integer progress;

    /**
     * 预期执行时间
     */
    private Long expectedDuration;

    /**
     * 实际执行时间
     */
    private Long actualDuration;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 开始执行时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 从实体转换
     */
    public static TaskDetailResponse fromEntity(AsyncTask task) {
        if (task == null) {
            return null;
        }
        TaskDetailResponse response = new TaskDetailResponse();
        response.setId(task.getId());
        response.setTaskKey(task.getTaskKey());
        response.setTaskName(task.getTaskName());
        response.setTaskType(task.getTaskType());
        response.setParams(task.getParams());
        response.setResult(task.getResult());
        response.setStatus(TaskStatus.fromCode(task.getStatus()));
        response.setStatusDesc(TaskStatus.fromCode(task.getStatus()) != null 
            ? TaskStatus.fromCode(task.getStatus()).getDesc() : "未知");
        response.setErrorMsg(task.getErrorMsg());
        response.setProgress(task.getProgress());
        response.setExpectedDuration(task.getExpectedDuration());
        response.setActualDuration(task.getActualDuration());
        response.setCreateTime(task.getCreateTime());
        response.setStartTime(task.getStartTime());
        response.setEndTime(task.getEndTime());
        return response;
    }
}
