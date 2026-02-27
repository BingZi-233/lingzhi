package com.lingzhi.db.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BaseEntity 单元测试
 */
@DisplayName("基础实体类测试")
class BaseEntityTest {

    @Test
    @DisplayName("基础实体类应该有 id 字段")
    void shouldHaveIdField() {
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        assertEquals(1L, entity.getId());
    }

    @Test
    @DisplayName("基础实体类应该有创建时间字段")
    void shouldHaveCreateTimeField() {
        TestEntity entity = new TestEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        assertEquals(now, entity.getCreateTime());
    }

    @Test
    @DisplayName("基础实体类应该有更新时间字段")
    void shouldHaveUpdateTimeField() {
        TestEntity entity = new TestEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdateTime(now);
        assertEquals(now, entity.getUpdateTime());
    }

    @Test
    @DisplayName("基础实体类应该有创建者字段")
    void shouldHaveCreateByField() {
        TestEntity entity = new TestEntity();
        entity.setCreateBy(1L);
        assertEquals(1L, entity.getCreateBy());
    }

    @Test
    @DisplayName("基础实体类应该有更新者字段")
    void shouldHaveUpdateByField() {
        TestEntity entity = new TestEntity();
        entity.setUpdateBy(1L);
        assertEquals(1L, entity.getUpdateBy());
    }

    @Test
    @DisplayName("基础实体类应该有备注字段")
    void shouldHaveRemarkField() {
        TestEntity entity = new TestEntity();
        entity.setRemark("测试备注");
        assertEquals("测试备注", entity.getRemark());
    }

    @Test
    @DisplayName("基础实体类应该有删除标记字段")
    void shouldHaveDeletedField() {
        TestEntity entity = new TestEntity();
        entity.setDeleted(0);
        assertEquals(0, entity.getDeleted());
    }

    /**
     * 测试用实体类
     */
    static class TestEntity extends BaseEntity {
    }
}
