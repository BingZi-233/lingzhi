package com.lingzhi.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingzhi.common.result.Result;
import com.lingzhi.db.entity.BaseEntity;
import com.lingzhi.db.service.BaseService;
import com.lingzhi.web.dto.PageRequest;
import com.lingzhi.web.dto.PageResult;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;

/**
 * 基础 Controller
 * 
 * @param <T> 实体类型
 */
@Validated
public class BaseController<S extends BaseService<T>, T extends BaseEntity> {

    /**
     * 获取 Service
     */
    protected S getService() {
        return null; // 子类实现
    }

    /**
     * 分页查询
     */
    protected Result<PageResult<T>> page(PageRequest request) {
        return page(request, null);
    }

    /**
     * 分页查询（带条件）
     */
    protected Result<PageResult<T>> page(PageRequest request, LambdaQueryWrapper<T> wrapper) {
        IPage<T> page = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<T> result = getService().pageBy(page, wrapper);
        return Result.success(PageResult.of(result));
    }

    /**
     * 根据ID查询
     */
    protected Result<T> get(Long id) {
        T entity = getService().getById(id);
        if (entity == null) {
            return Result.error(404, "数据不存在");
        }
        return Result.success(entity);
    }

    /**
     * 新增
     */
    protected Result<Void> add(T entity) {
        getService().save(entity);
        return Result.success();
    }

    /**
     * 修改
     */
    protected Result<Void> update(T entity) {
        if (entity.getId() == null) {
            return Result.error(400, "ID不能为空");
        }
        getService().updateById(entity);
        return Result.success();
    }

    /**
     * 删除
     */
    protected Result<Void> delete(Long id) {
        getService().removeById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    protected Result<Void> delete(Long... ids) {
        getService().removeByIds(Arrays.asList(ids));
        return Result.success();
    }
}
