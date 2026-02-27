package com.lingzhi.monitor.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 监控注解单元测试
 */
@DisplayName("监控注解测试")
class MonitorAnnotationTest {

    @Test
    @DisplayName("RateLimiter 注解应该有 key 属性")
    void rateLimiterShouldHaveKey() throws NoSuchMethodException {
        java.lang.reflect.Method method = TestClass.class.getMethod("rateLimitTest");
        RateLimiter annotation = method.getAnnotation(RateLimiter.class);
        assertNotNull(annotation);
        assertEquals("api:user:list", annotation.key());
    }

    @Test
    @DisplayName("RateLimiter 注解应该有 permitsPerSecond 属性")
    void rateLimiterShouldHavePermitsPerSecond() throws NoSuchMethodException {
        java.lang.reflect.Method method = TestClass.class.getMethod("rateLimitTest");
        RateLimiter annotation = method.getAnnotation(RateLimiter.class);
        assertEquals(10.0, annotation.permitsPerSecond());
    }

    @Test
    @DisplayName("RateLimiter 默认每10个请求每秒")
    void rateLimiterDefaultPermitsPerSecond() throws NoSuchMethodException {
        java.lang.reflect.Method method = TestClass.class.getMethod("defaultRateLimitTest");
        RateLimiter annotation = method.getAnnotation(RateLimiter.class);
        assertEquals(10.0, annotation.permitsPerSecond());
    }

    @Test
    @DisplayName("Idempotent 注解应该有 key 属性")
    void idempotentShouldHaveKey() throws NoSuchMethodException {
        java.lang.reflect.Method method = TestClass.class.getMethod("idempotentTest");
        Idempotent annotation = method.getAnnotation(Idempotent.class);
        assertNotNull(annotation);
        assertEquals("'order:' + #request.orderNo", annotation.key());
    }

    @Test
    @DisplayName("Idempotent 注解默认过期时间是60秒")
    void idempotentDefaultExpire() throws NoSuchMethodException {
        java.lang.reflect.Method method = TestClass.class.getMethod("idempotentTest");
        Idempotent annotation = method.getAnnotation(Idempotent.class);
        assertEquals(60, annotation.expire());
    }

    @Test
    @DisplayName("Sensitive 注解应该有正确的脱敏类型")
    void sensitiveShouldHaveCorrectTypes() {
        assertNotNull(Sensitive.SensitiveType.NAME);
        assertNotNull(Sensitive.SensitiveType.PHONE);
        assertNotNull(Sensitive.SensitiveType.EMAIL);
        assertNotNull(Sensitive.SensitiveType.ID_CARD);
        assertNotNull(Sensitive.SensitiveType.BANK_CARD);
        assertNotNull(Sensitive.SensitiveType.ADDRESS);
    }

    static class TestClass {
        @RateLimiter(key = "api:user:list", permitsPerSecond = 10)
        public void rateLimitTest() {}

        @RateLimiter
        public void defaultRateLimitTest() {}

        @Idempotent(key = "'order:' + #request.orderNo")
        public void idempotentTest() {}
    }
}
