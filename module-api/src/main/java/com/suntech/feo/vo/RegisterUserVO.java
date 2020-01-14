package com.suntech.feo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.vo
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2020年01月14日 11:24 ------------ -------------- ---------------------------------
 */
@Data
public class RegisterUserVO {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;

}
