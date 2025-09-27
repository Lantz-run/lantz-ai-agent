package com.lantz.lantzaiagent.config;

import com.lantz.lantzaiagent.service.impl.UserInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Resource
    private UserInterceptor userInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns("/login", "/register"); // 排除不需要拦截的路径
    }
}