package com.suntech.feo.security;

import com.suntech.feo.config.response.BaseResponse;
import com.suntech.feo.config.response.ResultCode;
import com.suntech.feo.utils.IpUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.security
 * @Description :  权限不足处理类，返回403
 * @Author : chenlei
 * @Create Date : 2020年01月03日 11:01
 * ------------ -------------- ---------------------------------
 */
@Component("RestAuthenticationAccessDeniedHandler")
public class RestAuthenticationAccessDeniedHandler  implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException{
        //登陆状态下，权限不足执行该方法
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        // AJAX请求,使用response发送403
        if (IpUtil.isAjaxRequest(request)) {
            response.sendError(403);
        }else{
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    ResultCode.FORBIDDEN.getMsg());
        }
    }
}
