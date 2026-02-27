package com.lingzhi.security.aspect;

import com.lingzhi.core.exception.BaseException;
import com.lingzhi.security.annotation.RequiresPermissions;
import com.lingzhi.security.annotation.RequiresRoles;
import com.lingzhi.security.context.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 安全注解切面
 */
@Slf4j
@Aspect
@Component
public class SecurityAspect {

    /**
     * @RequiresPermissions 切面
     */
    @Around("@annotation(com.lingzhi.security.annotation.RequiresPermissions)")
    public Object aroundRequiresPermissions(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiresPermissions annotation = method.getAnnotation(RequiresPermissions.class);

        // 获取当前用户权限（TODO: 从 SecurityContext 或其他地方获取）
        // 这里简化处理，实际应该从数据库查询用户权限
        String[] requiredPermissions = annotation.value();
        
        // 检查是否有权限
        if (requiredPermissions != null && requiredPermissions.length > 0) {
            // TODO: 实际实现权限检查
            // boolean hasPermission = checkPermissions(requiredPermissions);
            // if (!hasPermission) {
            //     throw new BaseException(403, "没有权限");
            // }
            log.debug("权限检查: required={}", String.join(",", requiredPermissions));
        }

        return joinPoint.proceed();
    }

    /**
     * @RequiresRoles 切面
     */
    @Around("@annotation(com.lingzhi.security.annotation.RequiresRoles)")
    public Object aroundRequiresRoles(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiresRoles annotation = method.getAnnotation(RequiresRoles.class);

        String[] requiredRoles = annotation.value();
        RequiresRoles.Mode mode = annotation.mode();

        if (requiredRoles != null && requiredRoles.length > 0) {
            // TODO: 实际实现角色检查
            // 获取当前用户角色
            // boolean hasRole = checkRoles(requiredRoles, mode);
            // if (!hasRole) {
            //     throw new BaseException(403, "没有角色权限");
            // }
            log.debug("角色检查: required={}, mode={}", String.join(",", requiredRoles), mode);
        }

        return joinPoint.proceed();
    }
}
