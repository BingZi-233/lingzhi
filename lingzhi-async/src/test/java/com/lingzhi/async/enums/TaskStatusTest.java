package com.lingzhi.async.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TaskStatus 枚举单元测试
 */
@DisplayName("任务状态枚举测试")
class TaskStatusTest {

    @Test
    @DisplayName("TaskStatus 应该有 PENDING 状态")
    void shouldHavePendingStatus() {
        assertNotNull(TaskStatus.PENDING);
        assertEquals(0, TaskStatus.PENDING.getCode());
        assertEquals("待执行", TaskStatus.PENDING.getDesc());
    }

    @Test
    @DisplayName("TaskStatus 应该有 RUNNING 状态")
    void shouldHaveRunningStatus() {
        assertNotNull(TaskStatus.RUNNING);
        assertEquals(1, TaskStatus.RUNNING.getCode());
        assertEquals("执行中", TaskStatus.RUNNING.getDesc());
    }

    @Test
    @DisplayName("TaskStatus 应该有 SUCCESS 状态")
    void shouldHaveSuccessStatus() {
        assertNotNull(TaskStatus.SUCCESS);
        assertEquals(2, TaskStatus.SUCCESS.getCode());
        assertEquals("执行成功", TaskStatus.SUCCESS.getDesc());
    }

    @Test
    @DisplayName("TaskStatus 应该有 FAILED 状态")
    void shouldHaveFailedStatus() {
        assertNotNull(TaskStatus.FAILED);
        assertEquals(3, TaskStatus.FAILED.getCode());
        assertEquals("执行失败", TaskStatus.FAILED.getDesc());
    }

    @Test
    @DisplayName("TaskStatus 应该有 CANCELLED 状态")
    void shouldHaveCancelledStatus() {
        assertNotNull(TaskStatus.CANCELLED);
        assertEquals(4, TaskStatus.CANCELLED.getCode());
        assertEquals("已取消", TaskStatus.CANCELLED.getDesc());
    }

    @Test
    @DisplayName("TaskStatus 应该有 TIMEOUT 状态")
    void shouldHaveTimeoutStatus() {
        assertNotNull(TaskStatus.TIMEOUT);
        assertEquals(5, TaskStatus.TIMEOUT.getCode());
        assertEquals("已超时", TaskStatus.TIMEOUT.getDesc());
    }

    @Test
    @DisplayName("根据 code 获取枚举")
    void getEnumByCode() {
        assertEquals(TaskStatus.PENDING, TaskStatus.fromCode(0));
        assertEquals(TaskStatus.RUNNING, TaskStatus.fromCode(1));
        assertEquals(TaskStatus.SUCCESS, TaskStatus.fromCode(2));
        assertEquals(TaskStatus.FAILED, TaskStatus.fromCode(3));
        assertEquals(TaskStatus.CANCELLED, TaskStatus.fromCode(4));
        assertEquals(TaskStatus.TIMEOUT, TaskStatus.fromCode(5));
        assertNull(TaskStatus.fromCode(99));
    }

    @Test
    @DisplayName("isFinished - 成功状态应该是终止状态")
    void successShouldBeFinished() {
        assertTrue(TaskStatus.SUCCESS.isFinished());
    }

    @Test
    @DisplayName("isFinished - 失败状态应该是终止状态")
    void failedShouldBeFinished() {
        assertTrue(TaskStatus.FAILED.isFinished());
    }

    @Test
    @DisplayName("isFinished - 取消状态应该是终止状态")
    void cancelledShouldBeFinished() {
        assertTrue(TaskStatus.CANCELLED.isFinished());
    }

    @Test
    @DisplayName("isFinished - 超时状态应该是终止状态")
    void timeoutShouldBeFinished() {
        assertTrue(TaskStatus.TIMEOUT.isFinished());
    }

    @Test
    @DisplayName("isFinished - 待执行状态不是终止状态")
    void pendingShouldNotBeFinished() {
        assertFalse(TaskStatus.PENDING.isFinished());
    }

    @Test
    @DisplayName("isFinished - 执行中状态不是终止状态")
    void runningShouldNotBeFinished() {
        assertFalse(TaskStatus.RUNNING.isFinished());
    }

    @Test
    @DisplayName("isCancellable - 待执行状态可以取消")
    void pendingShouldBeCancellable() {
        assertTrue(TaskStatus.PENDING.isCancellable());
    }

    @Test
    @DisplayName("isCancellable - 执行中状态可以取消")
    void runningShouldBeCancellable() {
        assertTrue(TaskStatus.RUNNING.isCancellable());
    }

    @Test
    @DisplayName("isCancellable - 成功状态不可以取消")
    void successShouldNotBeCancellable() {
        assertFalse(TaskStatus.SUCCESS.isCancellable());
    }
}
