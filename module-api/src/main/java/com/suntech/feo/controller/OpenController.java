package com.suntech.feo.controller;

import com.suntech.feo.annotation.SkipToken;
import com.suntech.feo.config.response.BaseResponse;
import com.suntech.feo.config.response.ResultCode;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.controller
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 12:02
 * ------------    --------------    ---------------------------------
 */
@Api(value = "OpenController",tags = "开放接口")
@RestController
@RequestMapping("open")
public class OpenController {

    @SkipToken
    @GetMapping("hello/{name}")
    public BaseResponse<String> hello(@NotNull(message = "用户名字不能为空") @PathVariable("name") String name){
        String result =  " Hello " +name + ", Welcome to Sun-tech !";
        return new BaseResponse<>(ResultCode.SUCCESS, result);
    }

}
