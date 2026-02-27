package com.lingzhi.common.interceptor;

import com.lingzhi.core.util.IdUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * 请求追踪拦截器
 */
@Slf4j
@Component
public class TraceInterceptor implements HandlerInterceptor {

    private static final String TRACE_ID = "traceId";
    private static final String TRACE_HEADER = "X-Trace-Id";

    /**
     * 前置处理 - 请求进入前执行
     * 获取或生成 traceId 并放入 MDC，供日志追踪使用
     *
     * @param request  HTTP 请求对象
     * @param response HTTP 响应对象
     * @param handler  处理器
     * @return true 继续执行后续拦截器/控制器，false 中断请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 优先从请求头获取 traceId，若无则生成新的 UUID
        String traceId = request.getHeader(TRACE_HEADER);
        if (traceId == null || traceId.isEmpty()) {
            traceId = IdUtils.uuid();
        }
        // 将 traceId 放入 MDC，方便日志追踪
        MDC.put(TRACE_ID, traceId);
        // 将 traceId 放入响应头，方便前端追溯
        response.setHeader(TRACE_HEADER, traceId);
        return true;
    }

    /**
     * 请求完成后清理 - 移除 MDC 中的 traceId
     *
     * @param request  HTTP 请求对象
     * @param response HTTP 响应对象
     * @param handler  处理器
     * @param ex       异常对象（若有）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理 MDC，避免内存泄漏
        MDC.remove(TRACE_ID);
    }
}
