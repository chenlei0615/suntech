package com.suntech.feo.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.dtos
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2020年01月14日 11:55 ------------ -------------- ---------------------------------
 */
@Data
public class SysRoleDTO {
    @ApiModelProperty(name = "角色名称")
    private String roleName;

    public SysRoleDTO(String roleName) {
        this.roleName = roleName;
    }
}
