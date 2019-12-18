package com.suntech.feo.repository;

import com.suntech.feo.dtos.SysUserDTO;
import com.suntech.feo.entity.SysUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.repository
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 13:08
 * ------------    --------------    ---------------------------------
 */
public interface SysUserRepository extends JpaRepository<SysUserEntity,String>, JpaSpecificationExecutor {

    @Query(value = "select new com.suntech.feo.dtos.SysUserDTO(e.id,e.nickName,e.openId,e.avatarUrl," +
            "e.gender,e.country,e.province,e.city,e.language,e.telephone,e.username) from SysUserEntity e where e.city = ?1 and e.deleted = false")
    Page<SysUserDTO> findAllAndDeletedFalse(String city, Pageable pageable);
}
