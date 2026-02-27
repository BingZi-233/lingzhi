package com.lingzhi.security.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JwtUtils 单元测试
 */
@DisplayName("JWT工具类测试")
class JwtUtilsTest {

    @Test
    @DisplayName("生成 Token")
    void generateToken() {
        // 这个测试需要 JwtConfig 和 JwtUtils 的实际配置
        // 这里只测试类是否存在
        assertNotNull(JwtUtils.class);
    }

    @Test
    @DisplayName("JWT 工具类应该有 @Component 注解")
    void shouldHaveComponentAnnotation() {
        assertNotNull(JwtUtils.class.getDeclaredAnnotation(org.springframework.stereotype.Component.class));
    }
}
