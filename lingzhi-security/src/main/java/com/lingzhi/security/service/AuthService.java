package com.lingzhi.security.service;

import com.lingzhi.security.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 认证服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;

    /**
     * 登录验证（实际项目中应查询数据库验证密码）
     */
    public LoginResult login(String username, String password) {
        // TODO: 从数据库查询用户验证密码
        // 这里简化处理，假设用户名密码正确
        Long userId = 1L;
        
        String token = jwtUtils.generateToken(userId, username);
        
        log.info("用户登录: username={}, userId={}", username, userId);
        
        return new LoginResult(token, userId, username);
    }

    /**
     * 验证 Token
     */
    public boolean validateToken(String token) {
        return jwtUtils.validateToken(token);
    }

    /**
     * 刷新 Token
     */
    public String refreshToken(String token) {
        return jwtUtils.refreshToken(token);
    }

    /**
     * 登录结果
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class LoginResult {
        private String token;
        private Long userId;
        private String username;
    }
}
