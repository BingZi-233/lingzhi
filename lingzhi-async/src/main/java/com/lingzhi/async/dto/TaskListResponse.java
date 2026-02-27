package com.lingzhi.async.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 任务列表响应
 */
@Data
public class TaskListResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务列表
     */
    private List<TaskDetailResponse> list;

    /**
     * 总数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;
}
