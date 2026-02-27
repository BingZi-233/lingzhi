package com.lingzhi.security.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 配置
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "lingzhi.security.jwt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JwtConfig {

    private static final String SECRET_KEY = "lingzhi-secret-key-must-be-at-least-256-bits-long";
    private static final long DEFAULT_EXPIRATION = 3600000; // 1小时

    private final String secret;
    private final long expiration;

    public JwtConfig() {
        this(SECRET_KEY, DEFAULT_EXPIRATION);
    }

    public JwtConfig(String secret, long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    /**
     * 生成密钥
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 Token
     */
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
            .subject(userId.toString())
            .claim("username", username)
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(secretKey())
            .compact();
    }

    /**
     * 解析 Token
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
            .verifyWith(secretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * 获取用户ID
     */
    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 获取用户名
     */
    public String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            log.warn("JWT 验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查 Token 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 刷新 Token
     */
    public String refreshToken(String token) {
        Claims claims = parseToken(token);
        Long userId = Long.parseLong(claims.getSubject());
        String username = claims.get("username", String.class);
        return generateToken(userId, username);
    }
}
