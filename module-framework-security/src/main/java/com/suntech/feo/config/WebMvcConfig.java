package com.suntech.feo.config;

import com.suntech.feo.annotation.CurrentUserMethodArgumentResolver;
import com.suntech.feo.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
* @Project : suntech
* @Package Name : com.suntech.feo.config
* @Description : 用户登陆后，经过jwt解密token，将用户信息放如，全文都可以得到当前登陆者信息
* @Author : chenlei
* @Create Date : 2019年12月18日 12:27
* ------------    --------------    ---------------------------------
*/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
                .addPathPatterns("/**")
                .excludePathPatterns("/register")
                .excludePathPatterns("/login")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    /**
     * 注册自定义参数解析器
     * @return
     */
    @Bean
    public HandlerMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserMethodArgumentResolver());
    }


}
