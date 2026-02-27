package com.lingzhi.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件存储配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "lingzhi.file")
public class FileProperties {

    /**
     * 存储类型：local/oss/minio
     */
    private String type = "local";

    /**
     * 本地存储配置
     */
    private LocalConfig local = new LocalConfig();

    /**
     * MinIO 配置
     */
    private MinioConfig minio = new MinioConfig();

    /**
     * 阿里云 OSS 配置
     */
    private OssConfig oss = new OssConfig();

    /**
     * 本地存储
     */
    @Data
    public static class LocalConfig {
        private String path = "/data/uploads";
        private String domain = "http://localhost:8080";
    }

    /**
     * MinIO 配置
     */
    @Data
    public static class MinioConfig {
        private String endpoint = "http://localhost:9000";
        private String accessKey = "minioadmin";
        private String secretKey = "minioadmin";
        private String bucketName = "lingzhi";
    }

    /**
     * 阿里云 OSS 配置
     */
    @Data
    public static class OssConfig {
        private String endpoint = "oss-cn-hangzhou.aliyuncs.com";
        private String accessKey;
        private String secretKey;
        private String bucketName = "lingzhi";
    }
}
