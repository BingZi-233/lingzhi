package com.lingzhi.async.service;

import com.lingzhi.async.dto.*;
import com.lingzhi.async.entity.AsyncTask;
import com.lingzhi.async.enums.TaskStatus;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 异步任务服务接口
 */
public interface AsyncTaskService {

    /**
     * 创建任务（仅创建记录，不执行）
     */
    AsyncTask createTask(TaskCreateRequest request, Long creatorId, String creatorName);

    /**
     * 创建并执行任务
     */
    AsyncTask createAndExecuteTask(TaskCreateRequest request, Callable<Object> callable, 
                                   Long creatorId, String creatorName);

    /**
     * 执行任务（根据taskKey）
     */
    void executeTask(String taskKey, Callable<Object> callable);

    /**
     * 执行任务（根据taskKey，超时控制）
     */
    Object executeTaskWithResult(String taskKey, Callable<Object> callable, long timeout);

    /**
     * 查询任务详情
     */
    TaskDetailResponse getTaskDetail(String taskKey);

    /**
     * 查询任务（根据ID）
     */
    TaskDetailResponse getTaskById(Long taskId);

    /**
     * 查询任务列表
     */
    TaskListResponse getTaskList(Long creatorId, String taskType, TaskStatus status, 
                                  Integer pageNum, Integer pageSize);

    /**
     * 取消任务
     */
    boolean cancelTask(String taskKey, Long operatorId);

    /**
     * 删除任务
     */
    boolean deleteTask(String taskKey);

    /**
     * 更新任务进度
     */
    void updateProgress(String taskKey, Integer progress);

    /**
     * 更新任务结果
     */
    void updateResult(String taskKey, Object result);

    /**
     * 更新任务失败
     */
    void updateFailed(String taskKey, String errorMsg);

    /**
     * 获取任务执行结果（阻塞等待）
     */
    Object getTaskResult(String taskKey, long timeout);

    /**
     * 查询用户最近的任务列表
     */
    List<TaskDetailResponse> getRecentTasks(Long creatorId, int limit);
}
