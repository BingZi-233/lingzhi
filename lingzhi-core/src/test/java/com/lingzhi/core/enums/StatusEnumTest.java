package com.lingzhi.core.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StatusEnum 单元测试
 */
@DisplayName("状态枚举测试")
class StatusEnumTest {

    @Test
    @DisplayName("StatusEnum 应该有 NORMAL 状态")
    void shouldHaveNormalStatus() {
        assertNotNull(StatusEnum.NORMAL);
        assertEquals(0, StatusEnum.NORMAL.getCode());
        assertEquals("正常", StatusEnum.NORMAL.getDesc());
    }

    @Test
    @DisplayName("StatusEnum 应该有 DISABLED 状态")
    void shouldHaveDisabledStatus() {
        assertNotNull(StatusEnum.DISABLED);
        assertEquals(1, StatusEnum.DISABLED.getCode());
        assertEquals("禁用", StatusEnum.DISABLED.getDesc());
    }

    @Test
    @DisplayName("StatusEnum 应该有 DELETED 状态")
    void shouldHaveDeletedStatus() {
        assertNotNull(StatusEnum.DELETED);
        assertEquals(2, StatusEnum.DELETED.getCode());
        assertEquals("已删除", StatusEnum.DELETED.getDesc());
    }

    @Test
    @DisplayName("根据 code 获取枚举")
    void getEnumByCode() {
        assertEquals(StatusEnum.NORMAL, StatusEnum.of(0));
        assertEquals(StatusEnum.DISABLED, StatusEnum.of(1));
        assertEquals(StatusEnum.DELETED, StatusEnum.of(2));
        assertNull(StatusEnum.of(99));
        assertNull(StatusEnum.of(null));
    }

    @Test
    @DisplayName("判断是否为正常状态")
    void isNormalTest() {
        assertTrue(StatusEnum.NORMAL.isNormal());
        assertFalse(StatusEnum.DISABLED.isNormal());
    }

    @Test
    @DisplayName("判断是否为禁用状态")
    void isDisabledTest() {
        assertTrue(StatusEnum.DISABLED.isDisabled());
        assertFalse(StatusEnum.NORMAL.isDisabled());
    }

    @Test
    @DisplayName("判断是否为已删除状态")
    void isDeletedTest() {
        assertTrue(StatusEnum.DELETED.isDeleted());
        assertFalse(StatusEnum.NORMAL.isDeleted());
    }

    @Test
    @DisplayName("获取所有状态")
    void getAllTest() {
        StatusEnum[] values = StatusEnum.values();
        assertEquals(3, values.length);
    }
}
