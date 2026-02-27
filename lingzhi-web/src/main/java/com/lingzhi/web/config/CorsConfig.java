package com.lingzhi.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS 跨域配置
 */
@Configuration
@ConditionalOnProperty(prefix = "lingzhi.web.cors", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CorsConfig {

    /**
     * CORS 过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许的来源
        config.addAllowedOriginPattern("*");
        
        // 允许的头部
        config.addAllowedHeader("*");
        
        // 允许的方法
        config.addAllowedMethod("*");
        
        // 允许凭证
        config.setAllowCredentials(true);
        
        // 预检请求缓存时间
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
