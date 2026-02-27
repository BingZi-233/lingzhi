package com.lingzhi.starter;

import com.lingzhi.async.config.AsyncAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

/**
 * 灵芝脚手架自动配置类
 * Spring Boot 自动配置入口，自动导入所需的组件配置
 * 
 * 功能模块：
 * - 异步任务模块 (AsyncAutoConfiguration)
 */
@Slf4j
@AutoConfiguration
@ConditionalOnProperty(prefix = "lingzhi", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({
    AsyncAutoConfiguration.class
})
public class LingzhiAutoConfiguration {

    /**
     * 构造器初始化日志输出
     */
    public LingzhiAutoConfiguration() {
        log.info("========================================");
        log.info("  灵芝 Spring Boot 脚手架 1.0.0 启动中  ");
        log.info("========================================");
    }
}
