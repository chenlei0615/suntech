package com.suntech.feo.controller.user;

import com.suntech.feo.annotation.SkipToken;
import com.suntech.feo.config.response.BaseResponse;
import com.suntech.feo.config.response.ResultCode;
import com.suntech.feo.dtos.LoginUserDTO;
import com.suntech.feo.dtos.SysUserDTO;
import com.suntech.feo.entity.user.SysUserEntity;
import com.suntech.feo.service.JwtUtils;
import com.suntech.feo.service.SysUserService;
import com.suntech.feo.vo.LoginUserVO;
import com.suntech.feo.vo.PageConditionVO;
import com.suntech.feo.vo.RegisterUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private AuthenticationManager authenticationManager;



    @Autowired
    private JwtUtils jwtUtils;

    @ApiOperation("登录")
    @PostMapping("/login")
//    @NoRepeatSubmit
    @SkipToken
    public BaseResponse<LoginUserDTO> login(@Valid LoginUserVO loginUserVO){
        //用户验证
        UsernamePasswordAuthenticationToken request = sysUserService.login(loginUserVO.getUsername(), loginUserVO.getPassword());
        //存储认证信息
        Authentication authentication = authenticationManager.authenticate(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        final SysUserDTO user = (SysUserDTO) authentication;
        final String token = jwtUtils.generateToken(user);
        LoginUserDTO result = LoginUserDTO.builder().sysUserDTO(user).token(token).build();
        return new BaseResponse<>(ResultCode.SUCCESS, result);
    }

    /**
     * 注册
     * @param registerUser
     * @return
     */
    @ApiOperation("注册")
    @PostMapping("/register")
    @SkipToken
    public BaseResponse<LoginUserDTO> registerUser(@RequestBody RegisterUserVO registerUser){
        LoginUserDTO result = sysUserService.register(registerUser.getUsername(),registerUser.getPassword());
        final String token = jwtUtils.generateToken(result.getSysUserDTO());
        result.setToken(token);
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
