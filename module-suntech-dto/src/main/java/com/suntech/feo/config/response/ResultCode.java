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
    ,FORBIDDEN(403,"token失效")
    ,BIND_ERROR(400,"参数不合法")
    ,NO_TOKEN(401,"无token，请重新登录")
    ,ERROR_TOKEN(401,"token异常，请重新登陆")
    ,BACKEND_EXCEPTION(501,"招聘系统异常")
    ,YET_SEND_POST(10010,"已经投递过该岗位，会有HR联系您")
    ,POST_OVERDUE(10011,"岗位已经下架，欢迎选择其他岗位")

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
