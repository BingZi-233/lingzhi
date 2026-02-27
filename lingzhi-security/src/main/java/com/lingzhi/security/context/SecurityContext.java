package com.lingzhi.security.context;

/**
 * 安全上下文
 * 
 * 获取当前登录用户信息
 */
public class SecurityContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    /**
     * 设置当前用户
     */
    public static void setUser(Long userId, String username) {
        USER_ID.set(userId);
        USERNAME.set(username);
    }

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        return USER_ID.get();
    }

    /**
     * 获取当前用户名
     */
    public static String getUsername() {
        return USERNAME.get();
    }

    /**
     * 清除用户信息
     */
    public static void clear() {
        USER_ID.remove();
        USERNAME.remove();
    }

    /**
     * 是否已登录
     */
    public static boolean isAuthenticated() {
        return USER_ID.get() != null;
    }
}
