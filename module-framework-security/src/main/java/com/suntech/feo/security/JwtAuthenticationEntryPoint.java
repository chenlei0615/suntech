package com.suntech.feo.security;

import com.suntech.feo.config.response.ResultCode;
import com.suntech.feo.utils.IpUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.security
 * @Description : 认证失败处理类，返回401
 * @Author : chenlei
 * @Create Date : 2020年01月03日 11:00
 * ------------ -------------- ---------------------------------
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        //验证为未登陆状态会进入此方法，认证错误
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        // AJAX请求,使用response发送403
        if (IpUtil.isAjaxRequest(request)) {
            response.sendError(401);
        }else{
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    ResultCode.AUTH_FAILED.getMsg());
        }
    }
}
