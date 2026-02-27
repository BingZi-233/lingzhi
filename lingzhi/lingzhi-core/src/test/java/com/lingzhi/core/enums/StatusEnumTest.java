package com.lingzhi.core.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StatusEnum 枚举单元测试
 */
@DisplayName("通用状态枚举测试")
class StatusEnumTest {

    @Test
    @DisplayName("NORMAL - 验证枚举值")
    void normalShouldHaveCorrectValues() {
        assertEquals(0, StatusEnum.NORMAL.getCode());
        assertEquals("正常", StatusEnum.NORMAL.getDesc());
    }

    @Test
    @DisplayName("DISABLED - 验证枚举值")
    void disabledShouldHaveCorrectValues() {
        assertEquals(1, StatusEnum.DISABLED.getCode());
        assertEquals("禁用", StatusEnum.DISABLED.getDesc());
    }

    @Test
    @DisplayName("DELETED - 验证枚举值")
    void deletedShouldHaveCorrectValues() {
        assertEquals(2, StatusEnum.DELETED.getCode());
        assertEquals("已删除", StatusEnum.DELETED.getDesc());
    }

    @Test
    @DisplayName("of - 根据code获取枚举-存在的code")
    void ofShouldReturnEnumForExistingCode() {
        assertEquals(StatusEnum.NORMAL, StatusEnum.of(0));
        assertEquals(StatusEnum.DISABLED, StatusEnum.of(1));
        assertEquals(StatusEnum.DELETED, StatusEnum.of(2));
    }

    @Test
    @DisplayName("of - 根据code获取枚举-不存在的code返回null")
    void ofShouldReturnNullForNonExistingCode() {
        assertNull(StatusEnum.of(99));
        assertNull(StatusEnum.of(-1));
        assertNull(StatusEnum.of(100));
    }

    @Test
    @DisplayName("of - 根据code获取枚举-null返回null")
    void ofShouldReturnNullForNullCode() {
        assertNull(StatusEnum.of(null));
    }

    @Test
    @DisplayName("isNormal - 验证正常状态判断")
    void isNormalShouldReturnTrueForNormal() {
        assertTrue(StatusEnum.NORMAL.isNormal());
        assertFalse(StatusEnum.DISABLED.isNormal());
        assertFalse(StatusEnum.DELETED.isNormal());
    }

    @Test
    @DisplayName("isDisabled - 验证禁用状态判断")
    void isDisabledShouldReturnTrueForDisabled() {
        assertFalse(StatusEnum.NORMAL.isDisabled());
        assertTrue(StatusEnum.DISABLED.isDisabled());
        assertFalse(StatusEnum.DELETED.isDisabled());
    }

    @Test
    @DisplayName("isDeleted - 验证删除状态判断")
    void isDeletedShouldReturnTrueForDeleted() {
        assertFalse(StatusEnum.NORMAL.isDeleted());
        assertFalse(StatusEnum.DISABLED.isDeleted());
        assertTrue(StatusEnum.DELETED.isDeleted());
    }

    @Test
    @DisplayName("values - 验证所有枚举值数量")
    void valuesShouldContainAllEnums() {
        assertEquals(3, StatusEnum.values().length);
    }
}
