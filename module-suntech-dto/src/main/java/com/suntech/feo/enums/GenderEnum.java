package com.suntech.feo.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.enums
 * @Description : 性别枚举
 * @Author : chenlei
 * @Create Date : 2019年12月18日 17:28
 * ------------    --------------    ---------------------------------
 */
public enum GenderEnum {
    MAN(1,"男"),
    WOMEN(0,"女")
    ;

    @Getter
    @Setter
    private Integer type;

    @Getter
    @Setter
    private String name;

    GenderEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

}
