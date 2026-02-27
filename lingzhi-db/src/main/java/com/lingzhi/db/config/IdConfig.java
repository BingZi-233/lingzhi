package com.lingzhi.db.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 分布式ID配置
 */
@Configuration
@ConditionalOnProperty(prefix = "lingzhi.db.id", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({
    com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class
})
public class IdConfig {
    // MyBatis Plus 自动配置已包含 ID 生成器
    // 配置项：lingzhi.db.id.worker-id, datacenter-id
}
