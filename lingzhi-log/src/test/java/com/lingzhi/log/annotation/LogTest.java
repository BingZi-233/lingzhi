package com.lingzhi.log.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Log 注解单元测试
 */
@DisplayName("操作日志注解测试")
class LogTest {

    @Test
    @DisplayName("Log 注解应该有 title 属性")
    void shouldHaveTitle() {
        try {
            java.lang.reflect.Method method = TestClass.class.getMethod("testMethod");
            Log log = method.getAnnotation(Log.class);
            assertNotNull(log);
            assertEquals("用户管理", log.title());
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        }
    }

    @Test
    @DisplayName("Log 注解应该有 businessType 属性")
    void shouldHaveBusinessType() {
        try {
            java.lang.reflect.Method method = TestClass.class.getMethod("testMethod");
            Log log = method.getAnnotation(Log.class);
            assertNotNull(log);
            assertEquals(Log.BusinessType.INSERT, log.businessType());
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        }
    }

    @Test
    @DisplayName("Log 注解默认应该保存请求参数")
    void shouldSaveRequestParamsByDefault() {
        try {
            java.lang.reflect.Method method = TestClass.class.getMethod("testMethod");
            Log log = method.getAnnotation(Log.class);
            assertTrue(log.saveRequestParams());
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        }
    }

    @Test
    @DisplayName("Log 注解默认应该保存响应参数")
    void shouldSaveResponseParamsByDefault() {
        try {
            java.lang.reflect.Method method = TestClass.class.getMethod("testMethod");
            Log log = method.getAnnotation(Log.class);
            assertTrue(log.saveResponseParams());
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        }
    }

    @Test
    @DisplayName("BusinessType 枚举应该有正确的值")
    void businessTypeShouldHaveCorrectValues() {
        assertEquals(10, Log.BusinessType.values().length);
        assertNotNull(Log.BusinessType.INSERT);
        assertNotNull(Log.BusinessType.UPDATE);
        assertNotNull(Log.BusinessType.DELETE);
    }

    static class TestClass {
        @Log(title = "用户管理", businessType = Log.BusinessType.INSERT)
        public void testMethod() {
        }
    }
}
