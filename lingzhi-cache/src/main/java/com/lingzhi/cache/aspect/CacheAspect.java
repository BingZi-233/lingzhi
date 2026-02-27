package com.lingzhi.cache.aspect;

import com.lingzhi.cache.annotation.Cachable;
import com.lingzhi.cache.annotation.CacheEvict;
import com.lingzhi.cache.service.CacheService;
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
import java.util.concurrent.TimeUnit;

/**
 * 缓存注解切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheAspect {

    private final CacheService cacheService;
    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * @Cachable 切面
     */
    @Around("@annotation(com.lingzhi.cache.annotation.Cachable)")
    public Object aroundCachable(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Cachable annotation = method.getAnnotation(Cachable.class);

        String key = resolveKey(annotation.key(), annotation.value(), joinPoint, method);
        
        // 先从缓存获取
        Object cachedValue = cacheService.get(key);
        if (cachedValue != null) {
            log.debug("从缓存获取: key={}", key);
            return cachedValue;
        }

        // 执行方法
        Object result = joinPoint.proceed();

        // 存入缓存
        if (result != null) {
            if (annotation.expire() > 0) {
                cacheService.setEx(key, result, annotation.expire());
            } else {
                cacheService.set(key, result);
            }
            log.debug("存入缓存: key={}", key);
        }

        return result;
    }

    /**
     * @CacheEvict 切面
     */
    @Around("@annotation(com.lingzhi.cache.annotation.CacheEvict)")
    public Object aroundCacheEvict(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CacheEvict annotation = method.getAnnotation(CacheEvict.class);

        String key = resolveKey(annotation.key(), annotation.value(), joinPoint, method);

        // 执行方法
        Object result = joinPoint.proceed();

        // 清除缓存
        if (annotation.allEntries()) {
            // TODO: 清除所有缓存（需要记录缓存名称）
            log.debug("清除所有缓存: value={}", annotation.value());
        } else {
            cacheService.delete(key);
            log.debug("清除缓存: key={}", key);
        }

        return result;
    }

    /**
     * 解析缓存 key
     */
    private String resolveKey(String key, String value, ProceedingJoinPoint joinPoint, Method method) {
        if (key != null && !key.isEmpty()) {
            return resolveSpEL(key, joinPoint, method);
        }
        if (value != null && !value.isEmpty()) {
            return resolveSpEL(value, joinPoint, method);
        }
        // 默认使用方法签名
        return method.getDeclaringClass().getSimpleName() + ":" + method.getName();
    }

    /**
     * 解析 SpEL 表达式
     */
    private String resolveSpEL(String spELString, ProceedingJoinPoint joinPoint, Method method) {
        try {
            StandardEvaluationContext context = new StandardEvaluationContext();
            
            // 设置方法参数
            String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            if (paramNames != null) {
                for (int i = 0; i < paramNames.length; i++) {
                    context.setVariable(paramNames[i], joinPoint.getArgs()[i]);
                }
            }
            
            // 设置 #root 对象
            context.setVariable("root", joinPoint.getArgs());
            
            Object value = parser.parseExpression(spELString).getValue(context);
            return value != null ? value.toString() : spELString;
        } catch (Exception e) {
            log.warn("解析SpEL失败: {}", spELString, e);
            return spELString;
        }
    }
}
