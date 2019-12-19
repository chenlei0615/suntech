package com.suntech.feo.ip;

import com.suntech.feo.interceptor.IpAddressInterceptor;
import com.suntech.feo.utils.IpUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Project : sun-tech
 * @Package Name : com.suntech.feo.annotation
 * @Description : IP地址过滤器,只允许IP白名单内的请求访问actuator
 * @Author : chenlei
 * @Create Date : 2019年12月19日 14:41
 * ------------    --------------    ---------------------------------
 */
@Component
public class ActuatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (httpServletRequest.getRequestURI().contains("actuator")) {
            String ipAddress = IpUtil.getIpAddress(httpServletRequest);
            List<String> urlRules = IpAddressInterceptor.urlRules;
            boolean flag = IpUtil.matchIp(ipAddress, httpServletRequest, httpServletResponse, urlRules);
            if (flag) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        } else {
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }
    }
}
