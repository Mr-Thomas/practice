package com.tj.practice.config;

import com.tj.practice.common.util.jwtInterceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器 要声明拦截对象 和 拦截请求
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")    //拦截路径
                .excludePathPatterns("/**/login/**");   //放行
    }
}
