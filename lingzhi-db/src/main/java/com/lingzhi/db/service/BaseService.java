package com.lingzhi.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lingzhi.db.entity.BaseEntity;

import java.util.List;

/**
 * 基础 Service 接口
 * 
 * @param <T> 实体类型
 */
public interface BaseService<T extends BaseEntity> extends IService<T> {

    /**
     * 分页查询
     */
    IPage<T> pageBy(IPage<T> page);

    /**
     * 分页查询（带条件）
     */
    IPage<T> pageBy(IPage<T> page, com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<T> wrapper);

    /**
     * 根据条件查询单条记录
     */
    T getOne(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<T> wrapper);

    /**
     * 根据条件查询列表
     */
    List<T> list(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<T> wrapper);

    /**
     * 统计数量
     */
    long count(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<T> wrapper);

    /**
     * 根据条件更新
     */
    boolean update(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<T> wrapper,
                  T entity);

    /**
     * 根据条件删除
     */
    boolean remove(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<T> wrapper);
}
