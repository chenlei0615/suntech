package com.suntech.feo.entity.user;

import com.suntech.feo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.entity.user
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2020年01月03日 10:38
 * ------------ -------------- ---------------------------------
 */
@Data
@Table(name = "sys_role")
@Entity
public class SysRoleEntity extends BaseEntity {
    @ApiModelProperty(name = "角色名称")
    @Column(name = "role_name")
    private String roleName;

    @ManyToMany
    private List<SysUserEntity> users;
}
