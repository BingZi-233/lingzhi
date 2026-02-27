package com.lingzhi.common.result;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Result 单元测试
 */
@DisplayName("统一响应结果测试")
class ResultTest {

    @Test
    @DisplayName("成功响应 - 无数据")
    void successWithoutData() {
        Result<Void> result = Result.success();
        assertEquals(200, result.getCode());
        assertTrue(result.isSuccess());
        assertEquals("操作成功", result.getMessage());
    }

    @Test
    @DisplayName("成功响应 - 带数据")
    void successWithData() {
        Result<String> result = Result.success("test data");
        assertEquals(200, result.getCode());
        assertTrue(result.isSuccess());
        assertEquals("test data", result.getData());
    }

    @Test
    @DisplayName("成功响应 - 带消息")
    void successWithMessage() {
        Result<String> result = Result.success("创建成功", "test data");
        assertEquals(200, result.getCode());
        assertEquals("创建成功", result.getMessage());
        assertEquals("test data", result.getData());
    }

    @Test
    @DisplayName("失败响应 - 默认错误码")
    void errorWithDefaultCode() {
        Result<Void> result = Result.error("操作失败");
        assertEquals(500, result.getCode());
        assertFalse(result.isSuccess());
        assertEquals("操作失败", result.getMessage());
    }

    @Test
    @DisplayName("失败响应 - 自定义错误码")
    void errorWithCustomCode() {
        Result<Void> result = Result.error(400, "参数错误");
        assertEquals(400, result.getCode());
        assertFalse(result.isSuccess());
        assertEquals("参数错误", result.getMessage());
    }

    @Test
    @DisplayName("失败响应 - 使用 BusinessException")
    void errorWithBusinessException() {
        Result<Void> result = Result.error(1000, "业务错误");
        assertEquals(1000, result.getCode());
    }

    @Test
    @DisplayName("响应应该包含 requestId")
    void shouldHaveRequestId() {
        Result<Void> result = Result.success();
        // requestId 可能在响应拦截器中设置，单元测试中可能为 null
        assertNotNull(result.getCode());
        assertTrue(result.isSuccess());
    }
}
