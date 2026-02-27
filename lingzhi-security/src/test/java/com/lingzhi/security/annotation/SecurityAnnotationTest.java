package com.lingzhi.security.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 安全注解单元测试
 */
@DisplayName("安全注解测试")
class SecurityAnnotationTest {

    @Test
    @DisplayName("RequiresPermissions 注解应该有 value 属性")
    void requiresPermissionsShouldHaveValue() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("permissionTest");
        RequiresPermissions annotation = method.getAnnotation(RequiresPermissions.class);
        assertNotNull(annotation);
        assertEquals("user:add", annotation.value()[0]);
    }

    @Test
    @DisplayName("RequiresRoles 注解应该有 value 属性")
    void requiresRolesShouldHaveValue() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("roleTest");
        RequiresRoles annotation = method.getAnnotation(RequiresRoles.class);
        assertNotNull(annotation);
        assertEquals("admin", annotation.value()[0]);
    }

    @Test
    @DisplayName("RequiresRoles 默认模式是 ALL")
    void requiresRolesDefaultModeIsAll() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("roleTest");
        RequiresRoles annotation = method.getAnnotation(RequiresRoles.class);
        assertEquals(RequiresRoles.Mode.ALL, annotation.mode());
    }

    @Test
    @DisplayName("Anonymous 注解应该存在")
    void anonymousAnnotationShouldExist() {
        assertNotNull(Anonymous.class);
    }

    static class TestClass {
        @RequiresPermissions("user:add")
        public void permissionTest() {}

        @RequiresRoles("admin")
        public void roleTest() {}
    }
}
