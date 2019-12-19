package com.suntech.feo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Project : sun-tech
 * @Package Name : com.suntech.feo.vo
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月19日 15:01
 * ------------    --------------    ---------------------------------
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVO {
    @ApiModelProperty("用户名")
    @NotNull(message = "用户名不能为空")
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @ApiModelProperty("密码")
    @NotNull(message = "密码不能为空")
    @NotEmpty(message = "密码不能为空")
    private String password;
}
