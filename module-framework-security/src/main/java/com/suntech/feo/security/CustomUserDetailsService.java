package com.suntech.feo.security;

import com.suntech.feo.entity.user.SysRoleEntity;
import com.suntech.feo.entity.user.SysUserEntity;
import com.suntech.feo.repository.user.SysUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.security
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2020年01月03日 11:02
 * ------------ -------------- ---------------------------------
 */
@Component(value="CustomUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private SysUserRepository sysUserRepository;

    public CustomUserDetailsService(SysUserRepository authMapper) {
        this.sysUserRepository = authMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        UserDetails userDetails = null;
        try {
            SysUserEntity userEntity = sysUserRepository.findByUsername(name);
            if (userEntity != null) {
                // 查询当前用户的权限
                List<SysRoleEntity> roles = userEntity.getRoles();
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                for (SysRoleEntity sysRoleEntity : roles) {
                    SimpleGrantedAuthority grant = new SimpleGrantedAuthority(sysRoleEntity.getRoleName());
                    authorities.add(grant);
                }
                userEntity.setAuthorities(authorities);
                userDetails = userEntity;
            } else {
                throw new UsernameNotFoundException("该用户不存在！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return userDetails;
    }
}
