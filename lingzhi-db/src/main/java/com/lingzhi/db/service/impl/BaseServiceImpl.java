package com.lingzhi.db.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingzhi.db.entity.BaseEntity;
import com.lingzhi.db.service.BaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

/**
 * 基础 Service 实现
 * 
 * @param <T> 实体类型
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> 
    extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public IPage<T> pageBy(IPage<T> page) {
        return pageBy(page, null);
    }

    @Override
    public IPage<T> pageBy(IPage<T> page, LambdaQueryWrapper<T> wrapper) {
        if (wrapper == null) {
            return this.page(page);
        }
        return this.page(page, wrapper);
    }

    @Override
    public T getOne(LambdaQueryWrapper<T> wrapper) {
        return this.getOne(wrapper, false);
    }

    @Override
    public List<T> list(LambdaQueryWrapper<T> wrapper) {
        if (wrapper == null) {
            return this.list();
        }
        return this.list(wrapper);
    }

    @Override
    public long count(LambdaQueryWrapper<T> wrapper) {
        if (wrapper == null) {
            return this.count();
        }
        return this.count(wrapper);
    }

    @Override
    public boolean update(LambdaQueryWrapper<T> wrapper, T entity) {
        if (wrapper == null) {
            return this.updateById(entity);
        }
        return this.update(entity, wrapper);
    }

    @Override
    public boolean remove(LambdaQueryWrapper<T> wrapper) {
        if (wrapper == null) {
            return this.removeById(null);
        }
        return this.remove(wrapper);
    }
}
