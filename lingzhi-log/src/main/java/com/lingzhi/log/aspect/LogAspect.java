package com.lingzhi.log.aspect;

import com.lingzhi.log.annotation.Log;
import com.lingzhi.security.context.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志切面
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Around("@annotation(com.lingzhi.log.annotation.Log)")
    public Object aroundLog(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log annotation = method.getAnnotation(Log.class);

        long startTime = System.currentTimeMillis();
        LocalDateTime startDateTime = LocalDateTime.now();
        
        // 获取操作人
        Long userId = SecurityContext.getUserId();
        String username = SecurityContext.getUsername();
        
        // 记录请求参数
        Map<String, Object> requestParams = new HashMap<>();
        if (annotation.saveRequestParams()) {
            requestParams = getParams(joinPoint);
        }

        Object result = null;
        Throwable error = null;
        
        try {
            // 执行方法
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            error = e;
            throw e;
        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            LocalDateTime endDateTime = LocalDateTime.now();
            
            // 记录日志
            saveLog(annotation, userId, username, requestParams, result, error, startDateTime, endDateTime, costTime);
        }
    }

    /**
     * 获取方法参数
     */
    private Map<String, Object> getParams(ProceedingJoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>();
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        
        if (paramNames != null && args != null) {
            for (int i = 0; i < paramNames.length; i++) {
                // 过滤敏感参数（密码等）
                String paramName = paramNames[i].toLowerCase();
                if (paramName.contains("password") || paramName.contains("token")) {
                    params.put(paramNames[i], "******");
                } else {
                    params.put(paramNames[i], args[i]);
                }
            }
        }
        
        return params;
    }

    /**
     * 保存日志
     */
    private void saveLog(Log annotation, Long userId, String username,
                         Map<String, Object> requestParams, Object result, 
                         Throwable error, LocalDateTime startTime, LocalDateTime endTime,
                         long costTime) {
        // 构建日志内容
        StringBuilder logContent = new StringBuilder();
        logContent.append("操作日志 | ");
        logContent.append("模块: ").append(annotation.title()).append(" | ");
        logContent.append("类型: ").append(annotation.businessType()).append(" | ");
        
        if (userId != null) {
            logContent.append("操作人: ").append(username).append("(").append(userId).append(") | ");
        }
        
        logContent.append("耗时: ").append(costTime).append("ms");
        
        if (error != null) {
            logContent.append(" | 状态: 失败 | 错误: ").append(error.getMessage());
            log.error(logContent.toString(), error);
        } else {
            logContent.append(" | 状态: 成功");
            log.info(logContent.toString());
        }
        
        // TODO: 可以存储到数据库
        // SysOperLog operLog = new SysOperLog();
        // operLog.setTitle(annotation.title());
        // operLog.setBusinessType(annotation.businessType().name());
        // operLog.setMethod(signature.getDeclaringTypeName() + "." + method.getName());
        // operLog.setRequestMethod(request.getMethod());
        // operLog.setOperator(userId);
        // operLog.setOperTime(startTime);
        // operLog.setCostTime(costTime);
        // operLog.setStatus(error == null ? 0 : 1);
        // operLog.setErrorMsg(error != null ? error.getMessage() : null);
    }
}
