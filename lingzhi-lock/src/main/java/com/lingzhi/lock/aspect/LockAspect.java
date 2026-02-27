package com.lingzhi.lock.aspect;

import com.lingzhi.lock.annotation.Lock;
import com.lingzhi.lock.service.LockService;
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
 * 分布式锁切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LockAspect {

    private final LockService lockService;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(com.lingzhi.lock.annotation.Lock)")
    public Object aroundLock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Lock annotation = method.getAnnotation(Lock.class);

        // 解析锁 key
        String lockKey = resolveKey(annotation.key(), annotation.prefix(), joinPoint, method);

        // 获取锁
        boolean acquired = lockService.tryLock(
            lockKey,
            annotation.waitTime(),
            annotation.leaseTime(),
            TimeUnit.SECONDS
        );

        if (!acquired) {
            throw new RuntimeException("获取锁失败: " + lockKey);
        }

        log.debug("获取分布式锁: key={}", lockKey);

        try {
            // 执行方法
            return joinPoint.proceed();
        } finally {
            // 释放锁
            lockService.unlock(lockKey);
            log.debug("释放分布式锁: key={}", lockKey);
        }
    }

    /**
     * 解析锁 key
     */
    private String resolveKey(String key, String prefix, ProceedingJoinPoint joinPoint, Method method) {
        String resolvedKey = resolveSpEL(key, joinPoint, method);
        return prefix + ":" + resolvedKey;
    }

    /**
     * 解析 SpEL 表达式
     */
    private String resolveSpEL(String spELString, ProceedingJoinPoint joinPoint, Method method) {
        try {
            StandardEvaluationContext context = new StandardEvaluationContext();
            
            String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            if (paramNames != null) {
                for (int i = 0; i < paramNames.length; i++) {
                    context.setVariable(paramNames[i], joinPoint.getArgs()[i]);
                }
            }
            
            Object value = parser.parseExpression(spELString).getValue(context);
            return value != null ? value.toString() : spELString;
        } catch (Exception e) {
            log.warn("解析SpEL失败: {}", spELString, e);
            return spELString;
        }
    }
}
