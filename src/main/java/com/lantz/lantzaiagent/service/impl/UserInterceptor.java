package com.lantz.lantzaiagent.service.impl;

import com.lantz.lantzaiagent.model.entity.User;
import com.lantz.lantzaiagent.service.UserContext;
import com.lantz.lantzaiagent.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 预处理用户 id
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.info("拦截器执行: {} {}", request.getMethod(), request.getRequestURI());

        try {
            User loginUser = userService.getLoginUser(request);
            if (loginUser != null) {
                Long userId = loginUser.getId();
                UserContext.setUserId(userId);
                logger.info("设置用户ID到上下文: {}", userId);
            } else {
                logger.warn("未获取到登录用户信息");
            }
        } catch (Exception e) {
            logger.error("拦截器处理用户信息失败", e);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.info("拦截器清理上下文");
        UserContext.clear();
    }
}