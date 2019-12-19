package com.suntech.feo.ip;

import com.suntech.feo.interceptor.IpAddressInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Project : sun-tech
 * @Package Name : com.suntech.feo.annotation
 * @Description : 拦截器配置
 * @Author : chenlei
 * @Create Date : 2019年12月19日 14:40
 * ------------    --------------    ---------------------------------
 */
public class InterceptorConfig implements WebMvcConfigurer{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipAddressInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    public IpAddressInterceptor ipAddressInterceptor() {
        return new IpAddressInterceptor();
    }
}
