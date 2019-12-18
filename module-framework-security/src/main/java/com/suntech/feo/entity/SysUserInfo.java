package com.suntech.feo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.entity
 * @Description :
 * @Author : chenlei
 * @Create Date : 2019年12月18日 12:27
 * ------------    --------------    ---------------------------------
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserInfo {

    private String id;

    @ApiModelProperty("微信openId")
    private String openId;

    @ApiModelProperty("微信名称")
    private String wechatName;

    @ApiModelProperty("用户类型 inner(内部员工) | hr | outer(外部员工)")
    private String userType;

    @ApiModelProperty("内部员工263账号")
    private String innerUser263;

    @ApiModelProperty("内部员工ehr员工id")
    private String ehrEmployeeId;

    @ApiModelProperty("推荐人")
    private String parentUserId;

    @ApiModelProperty("绑定的hrId")
    private Integer contactHrId;

    @ApiModelProperty("裂变级别")
    private Integer level;

    @ApiModelProperty("电话号码")
    private String telephone;

    @ApiModelProperty("用户真名")
    private String username;




}
