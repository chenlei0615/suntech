package com.suntech.feo.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Project : sun-tech
 * @Package Name : com.suntech.feo.annotation
 * @Description : 表单重复提交校验
 * @Author : chenlei
 * @Create Date : 2019年12月19日 13:50
 * ------------    --------------    ---------------------------------
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {
    int time() default 1 * 1000;
}
