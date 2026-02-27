package com.lingzhi.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Jackson 序列化/反序列化配置
 * 配置全局 ObjectMapper，统一 JSON 处理行为
 */
@AutoConfiguration
public class JacksonConfig {

    /**
     * 创建全局默认的 ObjectMapper
     * 主要配置：
     * 1. 注册 Java 8 时间模块，支持 LocalDateTime 等类型
     * 2. 忽略未知属性，避免 JSON 字段不存在时报错
     * 3. 禁用日期时间戳输出，改为字符串格式
     *
     * @return 配置好的 ObjectMapper 实例
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 注册 Java 8 时间模块，支持 LocalDateTime、Instant 等类型
        objectMapper.registerModule(new JavaTimeModule());

        // 忽略未知属性：JSON 中存在但 Java 对象没有的字段时不报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 禁用日期时间戳：输出格式为 "yyyy-MM-dd HH:mm:ss" 而非时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);

        return objectMapper;
    }
}
