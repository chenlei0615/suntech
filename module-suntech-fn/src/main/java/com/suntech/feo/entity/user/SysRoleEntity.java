package com.suntech.feo.entity.user;

import com.suntech.feo.entity.BaseEntity;
import com.suntech.feo.entity.menu.SysMenuEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleEntity extends BaseEntity {

    @ApiModelProperty(name = "角色名称")
    @Column(name = "role_name")
    private String roleName;

    @ApiModelProperty("角色相关用户")
    @ManyToMany(cascade=CascadeType.REFRESH,mappedBy="roles")
    private Set<SysUserEntity> users = new HashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_menu",joinColumns = @JoinColumn(name = "role_id")
            ,inverseJoinColumns = @JoinColumn(name = "menu_id"))
    private Set<SysMenuEntity> menus = new HashSet<>();

    public SysRoleEntity(String roleName) {
        this.roleName = roleName;
    }
}
