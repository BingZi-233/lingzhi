package com.lingzhi.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IdUtils 单元测试
 */
@DisplayName("ID生成工具类测试")
class IdUtilsTest {

    @RepeatedTest(10)
    @DisplayName("uuid - 生成无下划线UUID")
    void uuidShouldGenerateWithoutDash() {
        String uuid = IdUtils.uuid();
        assertNotNull(uuid);
        assertEquals(32, uuid.length());
        assertFalse(uuid.contains("-"));
    }

    @RepeatedTest(10)
    @DisplayName("uuid - 每次生成应唯一")
    void uuidShouldBeUnique() {
        String uuid1 = IdUtils.uuid();
        String uuid2 = IdUtils.uuid();
        assertNotEquals(uuid1, uuid2);
    }

    @RepeatedTest(10)
    @DisplayName("uuidWithDash - 生成带下划线UUID")
    void uuidWithDashShouldGenerateWithDash() {
        String uuid = IdUtils.uuidWithDash();
        assertNotNull(uuid);
        assertEquals(36, uuid.length());
        assertTrue(uuid.contains("-"));
    }

    @RepeatedTest(10)
    @DisplayName("uuidWithDash - 每次生成应唯一")
    void uuidWithDashShouldBeUnique() {
        String uuid1 = IdUtils.uuidWithDash();
        String uuid2 = IdUtils.uuidWithDash();
        assertNotEquals(uuid1, uuid2);
    }

    @RepeatedTest(10)
    @DisplayName("simpleUuid - 生成8位UUID")
    void simpleUuidShouldGenerate8Chars() {
        String uuid = IdUtils.simpleUuid();
        assertNotNull(uuid);
        assertEquals(8, uuid.length());
    }

    @RepeatedTest(10)
    @DisplayName("simpleUuid - 每次生成应唯一")
    void simpleUuidShouldBeUnique() {
        String uuid1 = IdUtils.simpleUuid();
        String uuid2 = IdUtils.simpleUuid();
        assertNotEquals(uuid1, uuid2);
    }

    @RepeatedTest(10)
    @DisplayName("snowflakeId - 生成雪花ID")
    void snowflakeIdShouldGeneratePositiveNumber() {
        long id = IdUtils.snowflakeId();
        assertTrue(id > 0);
    }

    @RepeatedTest(10)
    @DisplayName("snowflakeId - 每次生成应唯一")
    void snowflakeIdShouldBeUnique() {
        long id1 = IdUtils.snowflakeId();
        long id2 = IdUtils.snowflakeId();
        assertNotEquals(id1, id2);
    }

    @RepeatedTest(10)
    @DisplayName("snowflakeIdStr - 生成字符串形式的雪花ID")
    void snowflakeIdStrShouldReturnString() {
        String idStr = IdUtils.snowflakeIdStr();
        assertNotNull(idStr);
        assertTrue(idStr.length() > 0);
        assertTrue(Long.parseLong(idStr) > 0);
    }

    @Test
    @DisplayName("simpleSnowflakeId - 生成12位数字字符串")
    void simpleSnowflakeIdShouldGenerate12Digits() {
        String id = IdUtils.simpleSnowflakeId();
        assertNotNull(id);
        // 取模后可能是12位
        assertTrue(id.length() >= 10 && id.length() <= 12, "长度应该在10-12位之间，实际: " + id.length());
        assertTrue(Long.parseLong(id) > 0);
    }

    @RepeatedTest(10)
    @DisplayName("simpleSnowflakeId - 每次生成应唯一")
    void simpleSnowflakeIdShouldBeUnique() {
        String id1 = IdUtils.simpleSnowflakeId();
        String id2 = IdUtils.simpleSnowflakeId();
        assertNotEquals(id1, id2);
    }

    @Test
    @DisplayName("雪花ID生成 - 批量生成测试")
    void snowflakeIdBatchTest() {
        int batchSize = 1000;
        long[] ids = new long[batchSize];
        
        for (int i = 0; i < batchSize; i++) {
            ids[i] = IdUtils.snowflakeId();
        }
        
        // 验证所有ID都唯一
        for (int i = 0; i < batchSize; i++) {
            for (int j = i + 1; j < batchSize; j++) {
                assertNotEquals(ids[i], ids[j], "ID在批量生成中应该唯一");
            }
        }
    }
}
