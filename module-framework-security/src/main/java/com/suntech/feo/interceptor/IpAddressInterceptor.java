package com.suntech.feo.interceptor;

import com.suntech.feo.annotation.IpVerify;
import com.suntech.feo.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project : sun-tech
 * @Package Name : com.suntech.feo.annotation
 * @Description : IP地址拦截器,只允许IP白名单内的请求访问,校验标有IpVerify注解的类或者方法
 * @Author : chenlei
 * @Create Date : 2019年12月19日 14:36
 * ------------    --------------    ---------------------------------
 */
@Slf4j
public class IpAddressInterceptor implements HandlerInterceptor {
    public static List<String> urlRules = new ArrayList<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!urlRules.isEmpty()) {
            // 如果不是映射到方法直接通过
            if (!(handler instanceof HandlerMethod)) {
                return true;
            }
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //spring的报错类,直接通过
            Class<?> clazz = handlerMethod.getBeanType();
            if (clazz == BasicErrorController.class) {
                return true;
            }
            String ipAddr = IpUtil.getIpAddress(request);
            //检查类上是否有passIpVerify注释，有则需要验证
            if (clazz.isAnnotationPresent(IpVerify.class)) {
                IpVerify ipVerify = clazz.getAnnotation(IpVerify.class);
                if (ipVerify.required()) {
                    return IpUtil.matchIp(ipAddr, request, response,urlRules);
                }
            }
            //检查方法上是否有passIpVerify注释，有则需要验证
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(IpVerify.class)) {
                IpVerify ipVerify = method.getAnnotation(IpVerify.class);
                if (ipVerify.required()) {
                    return IpUtil.matchIp(ipAddr, request, response,urlRules);
                }
            }
        }
        return true;
    }
}
