package com.lingzhi.web.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PageRequest 单元测试
 */
@DisplayName("分页请求测试")
class PageRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("分页请求默认值为 pageNum=1, pageSize=10")
    void shouldHaveDefaultValues() {
        PageRequest request = new PageRequest();
        assertEquals(1, request.getPageNum());
        assertEquals(10, request.getPageSize());
    }

    @Test
    @DisplayName("分页请求可以设置自定义值")
    void shouldAllowCustomValues() {
        PageRequest request = new PageRequest();
        request.setPageNum(5);
        request.setPageSize(20);
        assertEquals(5, request.getPageNum());
        assertEquals(20, request.getPageSize());
    }

    @Test
    @DisplayName("分页请求 pageNum 不能小于1")
    void pageNumShouldNotBeLessThan1() {
        PageRequest request = new PageRequest();
        request.setPageNum(0);
        Set<ConstraintViolation<PageRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("分页请求 pageSize 不能超过1000")
    void pageSizeShouldNotExceed1000() {
        PageRequest request = new PageRequest();
        request.setPageSize(1001);
        Set<ConstraintViolation<PageRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("分页请求可以有排序字段")
    void shouldAllowOrderBy() {
        PageRequest request = new PageRequest();
        request.setOrderBy("createTime");
        request.setOrderDir("desc");
        assertEquals("createTime", request.getOrderBy());
        assertEquals("desc", request.getOrderDir());
    }
}
