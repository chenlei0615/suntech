package com.suntech.feo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Project : sun-tech
 * @Package Name : com.suntech.feo.annotation
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月19日 14:33
 * ------------    --------------    ---------------------------------
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IpVerify {
    boolean required() default true;
}
