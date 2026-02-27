package com.lingzhi.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lingzhi.common.interceptor.TraceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web MVC 配置类
 * 配置消息转换器、拦截器等 Web 相关的组件
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TraceInterceptor traceInterceptor;

    /**
     * 构造器注入 TraceInterceptor
     *
     * @param traceInterceptor 请求追踪拦截器
     */
    public WebMvcConfig(TraceInterceptor traceInterceptor) {
        this.traceInterceptor = traceInterceptor;
    }

    /**
     * 配置消息转换器
     * 添加自定义 Jackson 配置的消息转换器，确保 JSON 处理一致
     *
     * @param converters 消息转换器列表
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建 Jackson 消息转换器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        
        // 配置 ObjectMapper（与 JacksonConfig 保持一致）
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        converter.setObjectMapper(objectMapper);
        
        // 添加到转换器列表首位，优先使用
        converters.add(0, converter);
    }

    /**
     * 配置拦截器注册表
     * 添加 TraceInterceptor 用于请求追踪
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/health", "/actuator/**");
    }
}
