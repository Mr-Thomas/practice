package com.tj.practice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类
 * 密码加密
 * 添加了spring security依赖后，所有的地址都被spring security所控制了，我们目 前只是需要用到BCrypt密码加密的部分，所以我们要添加一个配置类，配置为所有地址 都可以匿名访问
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * @param http
     * @throws Exception
     *
     * authorizeRequests所有security全注解配置实现的开端,表示开始说明需要的权限
     * 需要的权限分两部分 1.拦截路径 2.该路径需要的权限
     * antMatchers表示拦截什么路径,permitAll任何权限都可以访问,直接放行所有
     * anyRequest任何请求,authenticated认证后才能访问
     * .and().csrf().disable();固定写法,表示csrf拦截失效
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
