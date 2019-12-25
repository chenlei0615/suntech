package com.suntech.feo.config.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.config.response
 * @Description : 提供统一的REST相应对象封装  用于提供返回对象的封装
 * @Author : chenlei
 * @Create Date : 2019年12月18日 12:08
 * ------------    --------------    ---------------------------------
 */
@ApiModel(value = "BaseResponse", description = "API接口的返回对象")
@Data
public class BaseResponse<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseResponse.class);
    public static final int CODE_OK = 200;

    /**
     * 是否请求成功 默认成功
     */
    private boolean success = true;

    /**
     * 是否成功
     */
    @ApiModelProperty(value = "返回状态码", name = "code", example = "true", required = true)
    private int code;

    /**
     * 说明
     */
    @ApiModelProperty(value = "返回的详细说明", name = "msg", example = "成功")
    private String msg;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回的数据", name = "data")
    private T data;

    public BaseResponse() {

    }

    public BaseResponse(ResultCode res) {
        this.code = res.getCode();
        this.msg = res.getMsg();
    }

    public BaseResponse(ResultCode res, T data) {
        this.code = res.getCode();
        this.msg = res.getMsg();
        this.data = data;
    }

    public BaseResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 提供成功的静态方法，方便使用
     * @return
     */
    public static BaseResponse success(){
        return success(null);
    }
    /**
     * 提供成功的静态方法，方便使用
     * @param data 希望作为返回值输出的结果对象
     * @return
     *                  返回success为true的相应对象
     */
    public static BaseResponse success(Object data){
        BaseResponse BaseResponse = new BaseResponse();
        BaseResponse.setSuccess(true);
        BaseResponse.setCode(CODE_OK);
        BaseResponse.setData(data);
        BaseResponse.setMsg("success");

        if(logger.isDebugEnabled()){
            logger.debug("输出响应：{}", BaseResponse.toString());
        }
        return BaseResponse;
    }


    /**
     * 提供失败对象的静态方法，方便输出
     * @return
     */
    public static BaseResponse failed(){
        BaseResponse BaseResponse = new BaseResponse();
        BaseResponse.setSuccess(false);
        BaseResponse.setCode(500);
        BaseResponse.setMsg("异常");
        return BaseResponse;
    }

    /**
     * 提供失败对象的静态方法，方便输出
     * @param code  错误码
     * @param msg   错误消息
     * @return
     */
    public static BaseResponse failed(int code, String msg){
        BaseResponse BaseResponse = new BaseResponse();
        BaseResponse.setSuccess(false);
        BaseResponse.setCode(code);
        BaseResponse.setMsg(msg);
        return BaseResponse;
    }
    /**
     * 提供失败对象的静态方法，方便输出
     * @param resultCode  结果枚举对象
     * @return
     */
    public static BaseResponse failed(ResultCode resultCode){
        return failed(resultCode.getCode(),resultCode.getMsg());
    }
    
}
