package com.suntech.feo.service;

import com.suntech.feo.dtos.SysUserDTO;
import com.suntech.feo.entity.SysUserEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.service
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 13:08
 * ------------    --------------    ---------------------------------
 */
public interface SysUserService {
    /**
     * findById
     * @param id
     * @return
     */
    @Cacheable(key = "'user_'+#id",value = "user")
    SysUserDTO findById(String id);

    /**
     * findByCity
     * @param city
     * @param page
     * @param size
     * @return
     */
    Page<SysUserDTO> findByCity(String city, Integer page, Integer size);

    /**
     * findByCity
     * @param city
     * @param page
     * @param size
     * @return
     */
    Page<SysUserEntity> findByCity1(String city, Integer page, Integer size);
}
