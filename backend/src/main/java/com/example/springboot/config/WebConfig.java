package com.example.springboot.config;

import com.example.springboot.common.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Web配置类
 * 用于注册拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/Login",
                        "/admin/login",
                        "/stu/login",
                        "/dormManager/login"
                );

        // 注意：必须排除登录接口，否则会陷入"需要登录才能访问，但访问登录接口又需要先登录"的死循环
    }
}
