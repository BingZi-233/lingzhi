package com.lingzhi.async.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingzhi.async.entity.AsyncTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 异步任务 Mapper
 */
@Mapper
public interface AsyncTaskMapper extends BaseMapper<AsyncTask> {

    /**
     * 根据taskKey查询任务
     */
    @Select("SELECT * FROM async_task WHERE task_key = #{taskKey} AND deleted = 0")
    AsyncTask selectByTaskKey(@Param("taskKey") String taskKey);

    /**
     * 查询用户的所有任务（按创建时间倒序）
     */
    @Select("SELECT * FROM async_task WHERE creator_id = #{creatorId} AND deleted = 0 ORDER BY create_time DESC LIMIT #{limit}")
    List<AsyncTask> selectByCreatorId(@Param("creatorId") Long creatorId, @Param("limit") Integer limit);

    /**
     * 查询超时任务
     */
    @Select("SELECT * FROM async_task WHERE status IN (0, 1) AND timeout > 0 AND " +
            "TIMESTAMPDIFF(SECOND, create_time, #{now}) * 1000 > timeout AND deleted = 0")
    List<AsyncTask> selectTimeoutTasks(@Param("now") LocalDateTime now);

    /**
     * 查询正在执行的任务
     */
    @Select("SELECT * FROM async_task WHERE status = 1 AND deleted = 0")
    List<AsyncTask> selectRunningTasks();
}
