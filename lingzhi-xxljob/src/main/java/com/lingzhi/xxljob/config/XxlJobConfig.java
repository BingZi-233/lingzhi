package com.lingzhi.xxljob.config;

import com.xxl.job.core.executor.XxlJobExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXL-Job 配置
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "xxl.job", name = "executor.appname")
public class XxlJobConfig {

    /**
     * XXL-Job 执行器配置
     */
    @Bean
    public XxlJobExecutor xxlJobExecutor() {
        XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
        
        // TODO: 从配置中读取
        // xxlJobExecutor.setAdminAddresses(adminAddresses);
        // xxlJobExecutor.setAppname(appname);
        // xxlJobExecutor.setPort(port);
        // xxlJobExecutor.setLogPath(logPath);
        // xxlJobExecutor.setAccessToken(accessToken);
        
        log.info("XXL-Job 执行器配置已加载");
        
        return xxlJobExecutor;
    }
}
