package com.suntech.feo.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Project : sun-tech
 * @Package Name : com.suntech.feo.dtos
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月19日 15:21
 * ------------    --------------    ---------------------------------
 */
@Data
@Builder
@AllArgsConstructor
public class LoginUserDTO {
    @ApiModelProperty("token")
    private String token;
    @ApiModelProperty("用户数据")
    private SysUserDTO sysUserDTO;
}
