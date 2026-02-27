package com.lingzhi.security.context;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SecurityContext 单元测试
 */
@DisplayName("安全上下文测试")
class SecurityContextTest {

    @Test
    @DisplayName("设置和获取用户信息")
    void setAndGetUser() {
        try {
            SecurityContext.setUser(1L, "testuser");
            assertEquals(1L, SecurityContext.getUserId());
            assertEquals("testuser", SecurityContext.getUsername());
        } finally {
            SecurityContext.clear();
        }
    }

    @Test
    @DisplayName("清除用户信息")
    void clearUser() {
        SecurityContext.setUser(1L, "testuser");
        SecurityContext.clear();
        assertNull(SecurityContext.getUserId());
        assertNull(SecurityContext.getUsername());
    }

    @Test
    @DisplayName("判断是否已登录 - 未登录")
    void isAuthenticatedFalse() {
        SecurityContext.clear();
        assertFalse(SecurityContext.isAuthenticated());
    }

    @Test
    @DisplayName("判断是否已登录 - 已登录")
    void isAuthenticatedTrue() {
        try {
            SecurityContext.setUser(1L, "testuser");
            assertTrue(SecurityContext.isAuthenticated());
        } finally {
            SecurityContext.clear();
        }
    }
}
