package com.lingzhi.security.util;

import com.lingzhi.security.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * JWT 工具类
 */
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtConfig jwtConfig;

    /**
     * 生成 Token
     */
    public String generateToken(Long userId, String username) {
        return jwtConfig.generateToken(userId, username);
    }

    /**
     * 解析 Token
     */
    public Long getUserId(String token) {
        return jwtConfig.getUserId(token);
    }

    /**
     * 获取用户名
     */
    public String getUsername(String token) {
        return jwtConfig.getUsername(token);
    }

    /**
     * 验证 Token
     */
    public boolean validateToken(String token) {
        return jwtConfig.validateToken(token);
    }

    /**
     * 检查是否过期
     */
    public boolean isTokenExpired(String token) {
        return jwtConfig.isTokenExpired(token);
    }

    /**
     * 刷新 Token
     */
    public String refreshToken(String token) {
        return jwtConfig.refreshToken(token);
    }
}
