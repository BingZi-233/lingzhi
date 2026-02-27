package com.lingzhi.monitor.aspect;

import com.lingzhi.cache.service.CacheService;
import com.lingzhi.core.exception.BaseException;
import com.lingzhi.monitor.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimiterAspect {

    private final CacheService cacheService;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(com.lingzhi.monitor.annotation.RateLimiter)")
    public Object aroundRateLimiter(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimiter annotation = method.getAnnotation(RateLimiter.class);

        String key = resolveKey(annotation.key(), joinPoint, method);
        double permitsPerSecond = annotation.permitsPerSecond();
        long timeout = annotation.timeout();

        // 使用本地限流（简单实现）
        // 生产环境可以使用 Redis 计数器
        String rateKey = "rate_limit:" + key;
        AtomicInteger counter = cacheService.get(rateKey);
        
        if (counter == null) {
            counter = new AtomicInteger(0);
            cacheService.set(rateKey, counter, 1, java.util.concurrent.TimeUnit.SECONDS);
        }

        int count = counter.incrementAndGet();
        
        if (count > permitsPerSecond) {
            counter.decrementAndGet();
            throw new BaseException(429, annotation.message());
        }

        try {
            return joinPoint.proceed();
        } finally {
            // 可选：定期清理计数器
        }
    }

    /**
     * 解析 key
     */
    private String resolveKey(String key, ProceedingJoinPoint joinPoint, Method method) {
        if (key != null && !key.isEmpty()) {
            try {
                StandardEvaluationContext context = new StandardEvaluationContext();
                String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
                if (paramNames != null) {
                    for (int i = 0; i < paramNames.length; i++) {
                        context.setVariable(paramNames[i], joinPoint.getArgs()[i]);
                    }
                }
                Object value = parser.parseExpression(key).getValue(context);
                return value != null ? value.toString() : key;
            } catch (Exception e) {
                log.warn("解析SpEL失败: {}", key, e);
            }
        }
        return method.getDeclaringClass().getSimpleName() + ":" + method.getName();
    }
}
