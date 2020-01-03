package com.suntech.feo.annotation;

import com.suntech.feo.dtos.SysUserDTO;
import com.suntech.feo.service.JwtUtils;
import com.suntech.feo.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
/**
* @Project : suntech
* @Package Name : com.suntech.feo.annotation
* @Description : 增加方法注入，将含有 @CurrentUser 注解的方法参数注入当前登录用户
* @Author : chenlei
* @Create Date : 2019年12月18日 12:27
* ------------    --------------    ---------------------------------
*/

public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {

        return methodParameter.getParameterType().isAssignableFrom(SysUserDTO.class)
                && methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Object loginUserObj = request.getAttribute("loginUser");

        String userStr = JSONUtils.objectToJson(loginUserObj);
        SysUserDTO loginUser = JSONUtils.fromJson(userStr, SysUserDTO.class);

        if(loginUser == null){
            //拦截器为空时，尝试从请求头中获取token并解析token的值
            String token = request.getHeader("Authorization");
            loginUser =  JSONUtils.fromJson(jwtUtils.getUserFromToken(token),SysUserDTO.class);
        }

        if(loginUser == null){
            throw new MissingServletRequestPartException("Authorization");
        }

        return loginUser;
    }
}
