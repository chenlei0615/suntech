package com.suntech.feo.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.suntech.feo.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.entity
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 13:06
 * ------------    --------------    ---------------------------------
 */
@Data
@Entity
@Table(name = "sys_user")
public class SysUserEntity extends BaseEntity implements UserDetails{

    @ApiModelProperty("微信名称")
    @Column(name = "nick_name",columnDefinition = "VARCHAR(255) COMMENT '微信名称' ")
    private String nickName;

    @ApiModelProperty("微信openId")
    @Column(name = "open_id",columnDefinition = "char(32) COMMENT '微信openId' ")
    private String openId;

    @ApiModelProperty("微信头像")
    @Column(name = "avatar_url",columnDefinition = "VARCHAR(255) COMMENT '微信头像' ")
    private String avatarUrl;

    @ApiModelProperty("性别")
    @Column(name = "gender",columnDefinition = "VARCHAR(8) COMMENT '性别' ")
    private String gender;

    @ApiModelProperty("用户所在地点所属国家")
    @Column(name = "country",columnDefinition = "VARCHAR(8) COMMENT '用户所在地点所属国家' ")
    private String country;

    @ApiModelProperty("用户所在地点所属省份")
    @Column(name = "province",columnDefinition = "VARCHAR(8) COMMENT '用户所在地点所属省份' ")
    private String province;

    @ApiModelProperty("用户所在地点所属城市")
    @Column(name = "city",columnDefinition = "VARCHAR(8) COMMENT '用户所在地点所属城市' ")
    private String city;

    @ApiModelProperty("语种")
    @Column(name="language",columnDefinition = "VARCHAR(64) COMMENT '语种' ")
    private String language;


    @ApiModelProperty("电话号")
    @Column(name="telephone",columnDefinition = "VARCHAR(11) COMMENT '电话号' ")
    private String telephone;

    @ApiModelProperty("用户真名")
    @Column(name="username",columnDefinition = "VARCHAR(128) COMMENT '用户真名' ")
    private String username;

    @ApiModelProperty("密码")
    @Column(name = "password")
    private String password;

    @ApiModelProperty("最近一次修改密码日期")
    private Date lastPasswordResetDate;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE
            , CascadeType.REFRESH },fetch = FetchType.LAZY)
    @JoinTable(name="sys_user_role",
            inverseJoinColumns=@JoinColumn(name="role_id"),
            joinColumns=@JoinColumn(name="user_id"))
    private List<SysRoleEntity> roles;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return false;
    }
}
