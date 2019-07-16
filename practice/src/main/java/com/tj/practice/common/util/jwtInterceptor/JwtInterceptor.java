package com.tj.practice.common.util.jwtInterceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * 实现 HandlerInterceptor   继承 HandlerInterceptorAdapter 效果一样
 *
 * 非常方便的实现自己的拦截器。他有三个方法：
 * 分别实现预处理、后处理（调用了Service并返回ModelAndView，但未进行页面渲 染）、返回处理（已经渲染了页面）
 * 在preHandle中，可以进行编码、安全控制等处理；
 * 在postHandle中，有机会修改ModelAndView；
 * 在afterCompletion中，可以根据ex是否为null判断是否发生了异常，进行日志记录
 */
@Component  //放入spring容器
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //无论如何都放行.能不能操作 在具体操作中判断
        //拦截器只负责把请求头中包含token的令牌进行一个解析
        String header = request.getHeader("Authorization");
        if(StringUtils.isNoneBlank(header)){
            //如果包含有Authorization头信息,对其进行解析
            //得到token
            String token = header.substring(6);
            //解析token [参照 :D:\Nancy\IdeaProjects\practice\src\test\java\com\tj\practice\Create_Jwt_token_test.java]
            //假如 token解析后得到role
            String roles ="admin";
            //匹配成功
            if(roles!=null && roles.equals("admin")){
                //放到request域中
                /**
                 * request在当次的请求的url之间有效一次传参数，速度快，缺点是参数只能取一次
                 *
                 * forward是请求转发，将现在的请求转交，他只能在同一个容器里使用 同时保存客户的请求状态。
                 * sendRedirect是重定向，可以在同一个容器里使用，也可以发送其他容器请求，但是会丢失请求信息。它等于重发一个请求。
                 *
                 * request.setAttribute是在请求域里面加了一个请求的参数，所以在sendRedirect以后是无法取到request.setAttribute的请求的。
                 *
                 */
                request.setAttribute("claims_admin",token);
                //具体业务就可以拿到token
            }
        }
        return true ;
    }
}
