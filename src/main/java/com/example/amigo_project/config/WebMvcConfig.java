package com.example.amigo_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/user/login","/user/join","/test","/css/**","/image/**", "/board/**","/smarteditor/**");
                .excludePathPatterns("/login", "/user/login", "/user/join", "/test",
                        "/css/**", "/image/**", "/admin/**", "/js/**", "/vendor/**");

        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/css/**", "/image/**", "/vendor/**", "/js/**");
    }
}
