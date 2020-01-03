package com.suntech.feo.controller.user;

import com.suntech.feo.annotation.SkipToken;
import com.suntech.feo.config.response.BaseResponse;
import com.suntech.feo.config.response.ResultCode;
import com.suntech.feo.dtos.LoginUserDTO;
import com.suntech.feo.dtos.SysUserDTO;
import com.suntech.feo.entity.user.SysUserEntity;
import com.suntech.feo.security.annotation.NoRepeatSubmit;
import com.suntech.feo.service.JwtUtils;
import com.suntech.feo.service.SysUserService;
import com.suntech.feo.vo.LoginUserVO;
import com.suntech.feo.vo.PageConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.controller
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 13:10
 * ------------    --------------    ---------------------------------
 */
@Api(value = "UserController",tags = "用户相关")
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private JwtUtils jwtUtils;

    @ApiOperation("登录")
    @PostMapping("/login")
    @NoRepeatSubmit
    @SkipToken
    public BaseResponse<LoginUserDTO> login(@Valid LoginUserVO loginUserVO){
        //用户验证
        final Authentication authentication = sysUserService.login(loginUserVO.getUsername(), loginUserVO.getPassword());
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        final SysUserDTO user = (SysUserDTO) authentication.getPrincipal();
        final String token = jwtUtils.generateToken(user);
        LoginUserDTO result = LoginUserDTO.builder().sysUserDTO(user).token(token).build();
        return new BaseResponse<>(ResultCode.SUCCESS, result);
    }

    @ApiOperation("通过id查询用户")
    @GetMapping("/{id}")
    public BaseResponse<SysUserDTO> findById(@PathVariable String id){
        SysUserDTO sysUserEntity = sysUserService.findById(id);
        return new BaseResponse<>(ResultCode.SUCCESS, sysUserEntity);
    }

    @ApiOperation("分页通过城市查询用户(推荐) - 法一")
    @GetMapping("/by_city")
    public BaseResponse<Page<SysUserDTO>> findByCity(@Validated PageConditionVO vo){
        Page<SysUserDTO> result = sysUserService.findByCity(vo.getCity(),vo.getPage(),vo.getSize());
        return new BaseResponse<>(ResultCode.SUCCESS, result);
    }

    @ApiOperation("分页通过城市查询用户 - 法二")
    @GetMapping("/by_city1")
    public BaseResponse<Page<SysUserEntity>> findByCity1(@Valid PageConditionVO vo){
        Page<SysUserEntity> result = sysUserService.findByCity1(vo.getCity(),vo.getPage(),vo.getSize());
        return new BaseResponse<>(ResultCode.SUCCESS, result);
    }



}
