package com.suntech.feo.interceptor;


import com.auth0.jwt.exceptions.JWTDecodeException;
import com.suntech.feo.annotation.SkipToken;
import com.suntech.feo.common.LoginUserContext;
import com.suntech.feo.config.response.ResultCode;
import com.suntech.feo.dtos.SysUserDTO;
import com.suntech.feo.exception.GlobalException;
import com.suntech.feo.service.JwtUtils;
import com.suntech.feo.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.interceptor
 * @Description : 用户登陆后，经过jwt解密token，将用户信息放如，全文都可以得到当前登陆者信息
 * @Author : chenlei
 * @Create Date : 2019年12月18日 12:27
 * ------------    --------------    ---------------------------------
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {

        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("Authorization");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();

        //检查是否有passtoken注释，有则跳过认证,其余的全部要走认证
        if (method.isAnnotationPresent(SkipToken.class)) {
            SkipToken skipToken = method.getAnnotation(SkipToken.class);
            if (skipToken.required()) {
                return true;
            }
        } else {
            // 解密token并获取token中的信息
            try {
                String userInfo = jwtUtils.getUserFromToken(token);
                Date expiresAt = jwtUtils.getExpirationDateFromToken(token);
                long currTimestamp = System.currentTimeMillis();
                long expireTimestamp = expiresAt.getTime();
                //token过期失效
                if (expireTimestamp < currTimestamp) {
                    throw new GlobalException(ResultCode.FORBIDDEN);
                }
                //解析token中带的用户信息
                SysUserDTO sysUserInfo = JSONUtils.fromJson(userInfo, SysUserDTO.class);
                LoginUserContext.setUserInfo(sysUserInfo);

            } catch (JWTDecodeException j) {
                throw new GlobalException(ResultCode.ERROR_TOKEN);
            }
            return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) {
        LoginUserContext.removeUser();
    }
}
