package com.suntech.feo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.annotation
 * @Description : 获取当前登陆者信息
 * @Author : chenlei
 * @Create Date : 2019年12月18日 12:27
 * ------------    --------------    ---------------------------------
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
