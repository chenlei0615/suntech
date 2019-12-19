package com.suntech.feo.service.impl;

import com.suntech.feo.common.RedisUtils;
import com.suntech.feo.dtos.SysUserDTO;
import com.suntech.feo.entity.SysUserEntity;
import com.suntech.feo.repository.SysUserRepository;
import com.suntech.feo.service.SysUserService;
import com.suntech.feo.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.service.impl
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 13:09
 * ------------    --------------    ---------------------------------
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private RedisUtils redisUtils;


    /**
     * findById
     * @param id
     * @return
     */
//    @Override
//    public SysUserDTO findById(String id) {
//        SysUserDTO sysUserDTO = null;
//         if(redisUtils.get(RedisConstants.CACHE_USER_PREFIX.concat(id)) !=null){
//             sysUserDTO = JSONUtils.fromJson(redisUtils.get(RedisConstants.CACHE_USER_PREFIX.concat(id)),SysUserDTO.class);
//         }else{
//             SysUserEntity userEntity= sysUserRepository.findById(id).orElse(null);
//             if(userEntity !=null){
//                 BeanUtils.copyProperties(userEntity,sysUserDTO);
//                 redisUtils.set(RedisConstants.CACHE_USER_PREFIX.concat(id),JSONUtils.objectToJson(userEntity));
//             }
//         }
//        return sysUserDTO;
//    }

    @Override
    public SysUserDTO findById(String id) {
        SysUserDTO sysUserDTO = new SysUserDTO();
        SysUserEntity userEntity = sysUserRepository.findById(id).orElse(null);
        if (userEntity != null) {
            BeanUtils.copyProperties(userEntity, sysUserDTO);
        }
        return sysUserDTO;
    }

    /**
     * findByCity
     * @param city
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<SysUserDTO> findByCity(String city, Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createDt");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        return sysUserRepository.findAllAndDeletedFalse(city,pageable);
    }


    /**
     * findByCity
     * @param city
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<SysUserEntity> findByCity1(String city, Integer page, Integer size) {
        // 法一
        Sort sort = Sort.by(Sort.Direction.DESC,"createDt");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Specification<SysUserEntity> specification = (Specification<SysUserEntity>) (root, query, criteriaBuilder) -> {
            Predicate deleted = criteriaBuilder.equal(root.get("deleted"), false);
            if (StringUtil.isNotEmpty(city)) {
                Predicate p1 = criteriaBuilder.equal(root.get("city"), city);
                deleted = criteriaBuilder.and(deleted, p1);
            }
            return deleted;
        };
        return sysUserRepository.findAll(specification,pageable);
    }

    @Override
    public SysUserDTO login(String username, String password) {
        SysUserEntity userEntity = sysUserRepository.findByUsername(username);
        if(userEntity == null){
            userEntity = new SysUserEntity();
            userEntity.setUsername(username);
            userEntity.setCity("shanghai");
            userEntity.setCountry("china");
            userEntity.setLanguage("cn");
            userEntity.setTelephone("18171665544");
            userEntity = sysUserRepository.save(userEntity);
        }
        SysUserDTO dto = new SysUserDTO();
        BeanUtils.copyProperties(userEntity,dto);
        return dto;
    }
}
