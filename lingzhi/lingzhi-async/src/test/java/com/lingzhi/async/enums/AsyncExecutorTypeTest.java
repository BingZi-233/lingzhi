package com.lingzhi.async.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AsyncExecutorType 枚举单元测试
 */
@DisplayName("异步执行器类型枚举测试")
class AsyncExecutorTypeTest {

    @Test
    @DisplayName("COMMON - 验证枚举值")
    void commonShouldHaveCorrectValues() {
        assertEquals("common", AsyncExecutorType.COMMON.getCode());
        assertEquals("通用执行器", AsyncExecutorType.COMMON.getDesc());
    }

    @Test
    @DisplayName("IO - 验证枚举值")
    void ioShouldHaveCorrectValues() {
        assertEquals("io", AsyncExecutorType.IO.getCode());
        assertEquals("IO密集型执行器", AsyncExecutorType.IO.getDesc());
    }

    @Test
    @DisplayName("CPU - 验证枚举值")
    void cpuShouldHaveCorrectValues() {
        assertEquals("cpu", AsyncExecutorType.CPU.getCode());
        assertEquals("CPU密集型执行器", AsyncExecutorType.CPU.getDesc());
    }

    @Test
    @DisplayName("FAST - 验证枚举值")
    void fastShouldHaveCorrectValues() {
        assertEquals("fast", AsyncExecutorType.FAST.getCode());
        assertEquals("快速执行器", AsyncExecutorType.FAST.getDesc());
    }

    @Test
    @DisplayName("SLOW - 验证枚举值")
    void slowShouldHaveCorrectValues() {
        assertEquals("slow", AsyncExecutorType.SLOW.getCode());
        assertEquals("慢速执行器", AsyncExecutorType.SLOW.getDesc());
    }

    @Test
    @DisplayName("MAIL - 验证枚举值")
    void mailShouldHaveCorrectValues() {
        assertEquals("mail", AsyncExecutorType.MAIL.getCode());
        assertEquals("邮件发送执行器", AsyncExecutorType.MAIL.getDesc());
    }

    @Test
    @DisplayName("PUSH - 验证枚举值")
    void pushShouldHaveCorrectValues() {
        assertEquals("push", AsyncExecutorType.PUSH.getCode());
        assertEquals("消息推送执行器", AsyncExecutorType.PUSH.getDesc());
    }

    @Test
    @DisplayName("FILE - 验证枚举值")
    void fileShouldHaveCorrectValues() {
        assertEquals("file", AsyncExecutorType.FILE.getCode());
        assertEquals("文件处理执行器", AsyncExecutorType.FILE.getDesc());
    }

    @Test
    @DisplayName("of - 根据code获取枚举-存在的code")
    void ofShouldReturnEnumForExistingCode() {
        assertEquals(AsyncExecutorType.COMMON, AsyncExecutorType.of("common"));
        assertEquals(AsyncExecutorType.IO, AsyncExecutorType.of("io"));
        assertEquals(AsyncExecutorType.CPU, AsyncExecutorType.of("cpu"));
        assertEquals(AsyncExecutorType.FAST, AsyncExecutorType.of("fast"));
        assertEquals(AsyncExecutorType.SLOW, AsyncExecutorType.of("slow"));
        assertEquals(AsyncExecutorType.MAIL, AsyncExecutorType.of("mail"));
        assertEquals(AsyncExecutorType.PUSH, AsyncExecutorType.of("push"));
        assertEquals(AsyncExecutorType.FILE, AsyncExecutorType.of("file"));
    }

    @Test
    @DisplayName("of - 根据code获取枚举-不存在的code返回COMMON")
    void ofShouldReturnCommonForNonExistingCode() {
        assertEquals(AsyncExecutorType.COMMON, AsyncExecutorType.of("unknown"));
        assertEquals(AsyncExecutorType.COMMON, AsyncExecutorType.of(""));
        assertEquals(AsyncExecutorType.COMMON, AsyncExecutorType.of(null));
    }

    @Test
    @DisplayName("of - 根据code获取枚举-大小写敏感")
    void ofShouldBeCaseSensitive() {
        assertEquals(AsyncExecutorType.COMMON, AsyncExecutorType.of("COMMON"));
        assertEquals(AsyncExecutorType.COMMON, AsyncExecutorType.of("Common"));
    }

    @Test
    @DisplayName("values - 验证所有枚举值数量")
    void valuesShouldContainAllEnums() {
        assertEquals(8, AsyncExecutorType.values().length);
    }
}
