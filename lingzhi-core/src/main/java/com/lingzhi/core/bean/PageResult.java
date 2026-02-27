package com.lingzhi.core.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Long pageNum;

    /**
     * 每页大小
     */
    private Long pageSize;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 额外数据
     */
    private Object extra;

    public PageResult() {
        this.list = new ArrayList<>();
    }

    public PageResult(List<T> list, Long total, Long pageNum, Long pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = (total + pageSize - 1) / pageSize;
    }

    /**
     * 空分页结果
     */
    public static <T> PageResult<T> empty() {
        return empty(1L, 10L);
    }

    /**
     * 空分页结果
     */
    public static <T> PageResult<T> empty(Long pageNum, Long pageSize) {
        return new PageResult<>(new ArrayList<>(), 0L, pageNum, pageSize);
    }

    /**
     * 判断是否有数据
     */
    public boolean hasData() {
        return list != null && !list.isEmpty();
    }

    /**
     * 判断是否有下一页
     */
    public boolean hasNext() {
        return pages != null && pageNum != null && pageNum < pages;
    }

    /**
     * 判断是否有上一页
     */
    public boolean hasPrevious() {
        return pageNum != null && pageNum > 1;
    }
}
