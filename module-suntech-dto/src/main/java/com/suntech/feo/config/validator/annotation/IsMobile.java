package com.suntech.feo.config.validator.annotation;

import com.suntech.feo.config.validator.IsMobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.config.response
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 12:11
 * ------------    --------------    ---------------------------------
 */

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint( validatedBy = {IsMobileValidator.class})//引进校验器
public @interface IsMobile {

    boolean required() default true;//默认不能为空
    //假如手机号码格式错误将抛出绑定异常
    String message() default "手机号码格式错误";//校验不通过输出信息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
