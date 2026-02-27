package com.lingzhi.file.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileProperties 单元测试
 */
@DisplayName("文件配置测试")
class FilePropertiesTest {

    @Test
    @DisplayName("文件配置默认类型是 local")
    void defaultTypeShouldBeLocal() {
        FileProperties properties = new FileProperties();
        assertEquals("local", properties.getType());
    }

    @Test
    @DisplayName("文件配置应该有本地存储配置")
    void shouldHaveLocalConfig() {
        FileProperties properties = new FileProperties();
        assertNotNull(properties.getLocal());
        assertEquals("/data/uploads", properties.getLocal().getPath());
        assertEquals("http://localhost:8080", properties.getLocal().getDomain());
    }

    @Test
    @DisplayName("文件配置应该有 MinIO 配置")
    void shouldHaveMinioConfig() {
        FileProperties properties = new FileProperties();
        assertNotNull(properties.getMinio());
        assertEquals("http://localhost:9000", properties.getMinio().getEndpoint());
    }

    @Test
    @DisplayName("文件配置应该有 OSS 配置")
    void shouldHaveOssConfig() {
        FileProperties properties = new FileProperties();
        assertNotNull(properties.getOss());
        assertEquals("oss-cn-hangzhou.aliyuncs.com", properties.getOss().getEndpoint());
    }

    @Test
    @DisplayName("设置存储类型")
    void setType() {
        FileProperties properties = new FileProperties();
        properties.setType("oss");
        assertEquals("oss", properties.getType());
    }

    @Test
    @DisplayName("设置本地存储路径")
    void setLocalPath() {
        FileProperties properties = new FileProperties();
        properties.getLocal().setPath("/custom/path");
        assertEquals("/custom/path", properties.getLocal().getPath());
    }
}
