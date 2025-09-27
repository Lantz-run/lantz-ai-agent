package com.lantz.lantzaiagent.service;

import org.springframework.stereotype.Component;

/**
 * 获取用户上下文 --- 用户id
 */
@Component
public class UserContext {
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }
    
    public static Long getUserId() {
        return USER_ID.get();
    }
    
    public static void clear() {
        USER_ID.remove();
    }

    // 新增：获取当前上下文副本
    public static ContextCopy getContextCopy() {
        return new ContextCopy(USER_ID.get());
    }

    // 新增：在目标线程设置上下文
    public static void setContext(ContextCopy copy) {
        if (copy != null && copy.userId != null) {
            USER_ID.set(copy.userId);
        }
    }

    // 上下文副本类
    public static class ContextCopy {
        private final Long userId;

        public ContextCopy(Long userId) {
            this.userId = userId;
        }

        public Long getUserId() {
            return userId;
        }
    }
}