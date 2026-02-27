package com.lingzhi.core.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 异常类单元测试
 */
@DisplayName("异常类测试")
class ExceptionTest {

    @Test
    @DisplayName("BusinessException - 消息构造")
    void businessExceptionWithMessage() {
        BusinessException exception = new BusinessException("业务错误");
        assertEquals("业务错误", exception.getMessage());
    }

    @Test
    @DisplayName("BusinessException - 错误码和消息构造")
    void businessExceptionWithCodeAndMessage() {
        BusinessException exception = new BusinessException(1001, "业务错误");
        assertEquals(1001, exception.getCode());
        assertEquals("业务错误", exception.getMessage());
    }

    @Test
    @DisplayName("NotFoundException - 消息构造")
    void notFoundExceptionWithMessage() {
        NotFoundException exception = new NotFoundException("资源不存在");
        assertEquals("资源不存在", exception.getMessage());
        assertEquals(404, exception.getCode());
    }

    @Test
    @DisplayName("BaseException - 错误码和消息构造")
    void baseExceptionWithCodeAndMessage() {
        BaseException exception = new BaseException(500, "系统错误");
        assertEquals(500, exception.getCode());
        assertEquals("系统错误", exception.getMessage());
    }

    @Test
    @DisplayName("BaseException - 错误码、消息和数据构造")
    void baseExceptionWithAllParams() {
        Object data = new Object();
        BaseException exception = new BaseException(500, "系统错误", data);
        assertEquals(500, exception.getCode());
        assertEquals("系统错误", exception.getMessage());
        assertEquals(data, exception.getData());
    }
}
