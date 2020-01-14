package com.suntech.feo.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.dtos
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 17:31
 * ------------    --------------    ---------------------------------
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserDTO {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("微信名称")
    private String nickName;

    @ApiModelProperty("微信openId")
    private String openId;

    @ApiModelProperty("微信头像")
    private String avatarUrl;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("用户所在地点所属国家")
    private String country;

    @ApiModelProperty("用户所在地点所属省份")
    private String province;

    @ApiModelProperty("用户所在地点所属城市")
    private String city;

    @ApiModelProperty("语种")
    private String language;

    @ApiModelProperty("电话号")
    private String telephone;

    @ApiModelProperty("用户真名")
    private String username;

}
