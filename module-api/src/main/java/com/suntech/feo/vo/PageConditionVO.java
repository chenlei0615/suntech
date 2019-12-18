package com.suntech.feo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.vo
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 17:38
 * ------------    --------------    ---------------------------------
 */
@Data
public class PageConditionVO {
    @ApiModelProperty("城市")
    private String city;
    @NotNull(message = "页数不能为空")
    @Min(1)
    private Integer page;
    @NotNull(message = "条数不能为空")
    @Max(20)
    private Integer size;
}
