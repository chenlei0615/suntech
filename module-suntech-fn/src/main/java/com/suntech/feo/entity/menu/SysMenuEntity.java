package com.suntech.feo.entity.menu;

import com.suntech.feo.entity.BaseEntity;
import com.suntech.feo.entity.user.SysRoleEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.entity.menu
 * @Description : 系统菜单
 * @Author : chenlei
 * @Create Date : 2020年01月07日 14:10
 * ------------ -------------- ---------------------------------
 */
@Data
@Entity
@Table(name = "sys_menu")
public class SysMenuEntity extends BaseEntity {

    @ApiModelProperty("菜单名字")
    private String menuName;

    @ApiModelProperty("菜单等级")
    private int level;

    @ApiModelProperty("菜单上级id")
    @Column(name = "pid")
    private String pid;

    @ApiModelProperty("菜单备注")
    private String remarks;

    @ApiModelProperty("菜单地址")
    private String menuUrl;

    @ApiModelProperty("菜单状态")
    private Integer status;

    @ApiModelProperty(name="菜单类型")
    private Integer type;

    @ManyToMany(mappedBy = "menus")
    @ApiModelProperty(name="菜单相关角色")
    Set<SysRoleEntity> roles = new HashSet<>();

}
