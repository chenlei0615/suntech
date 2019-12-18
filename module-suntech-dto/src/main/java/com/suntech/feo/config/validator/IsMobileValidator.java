package com.suntech.feo.config.validator;

import com.suntech.feo.config.validator.annotation.IsMobile;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.config.validator
 * @Description : TODO
 * @Author : xiekun
 * @Create Date : 2019年11月25日 22:09
 * @ModificationHistory Who   When     What
 * ------------    --------------    ---------------------------------
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    /**
     * 手机号正则
     */
    static String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
    private static final Pattern MOBILE_PATTERN = Pattern.compile(regex);

    /**
     * 初始化指定注解接口
     * @param isMobile
     */
    @Override
    public void initialize(IsMobile isMobile) {
        required = isMobile.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            return isMobile(value);
        } else {
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return isMobile(value);
            }
        }
    }

    private static boolean isMobile(String src){
        if(StringUtils.isEmpty(src)){
            return false;
        }
        Matcher m = MOBILE_PATTERN.matcher(src);
        return m.matches();
    }

}
