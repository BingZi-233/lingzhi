package com.lingzhi.monitor.aspect;

import com.lingzhi.cache.service.CacheService;
import com.lingzhi.core.exception.BaseException;
import com.lingzhi.monitor.annotation.Idempotent;
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

/**
 * 幂等切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {

    private final CacheService cacheService;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(com.lingzhi.monitor.annotation.Idempotent)")
    public Object aroundIdempotent(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Idempotent annotation = method.getAnnotation(Idempotent.class);

        String key = resolveKey(annotation.key(), joinPoint, method);
        String idempotentKey = "idempotent:" + key;

        // 检查是否已存在
        Boolean exists = cacheService.hasKey(idempotentKey);
        
        if (exists != null && exists) {
            log.warn("请求已存在（幂等）: key={}", idempotentKey);
            throw new BaseException(409, annotation.message());
        }

        // 设置幂等标记
        cacheService.setEx(idempotentKey, "1", annotation.expire());

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            // 失败时删除幂等标记
            cacheService.delete(idempotentKey);
            throw e;
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
