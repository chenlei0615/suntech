package com.suntech.feo.config.response;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.config.response
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 12:11
 * ------------    --------------    ---------------------------------
 */
public enum ResultCode {
    SUCCESS(Result.SUCCESS,"成功")
    ,ERROR(Result.ERROR,"系统错误")
    ,BIND_ERROR(400,"请求失败")
    ,AUTH_FAILED(401,"认证失败")
    ,ERROR_TOKEN(402,"token异常，请重新登陆")
    ,FORBIDDEN(403,"用户验证失败")
    ,IP_NOT_PERMIT(501,"IP address is not permit")
    ,REPEAT_SUBMIT(601,"请勿重复提交!")

    ;

    private Integer code;
    private String msg;


    /**
     * 拿到Result对象
     * @return
     */
    public Result get(){
        return new Result().setCode(this.code).setMsg(this.msg);
    }

    ResultCode(Integer status, String msg){
        this.code = status;
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public static ResultCode getByCode(Integer status){
        ResultCode tab = null;
        ResultCode[] values = ResultCode.values();
        for (ResultCode value : values) {
            if (status.equals(value.code)) {
                tab = value;
            }
        }
        return tab;
    }
}
