package com.bj.exceptionCode;

public enum BASICTRANCODE {
    UNKONWEXCEPTION(1000,"系统异常"),
    VALIDEXCEPTION(1001,"参数检验失败"),
    SMSEXCEPTION(1002,"验证码获取频繁，请稍后再试"),
    PHONE_EXIST_EXCEPTION(1003, "手机号已存在"),
    USER_EXIST_EXCEPTION(1004,"用户已存在"),
    SOCIALUSER_LOGIN_ERROR(1005,"社交用户授权失败" ),
    LOGINACTT_PASSWORD_ERROR(1006,"账户名或者密码错误"),
    NOT_STOCK_EXCEPTION(1007, "商品库存不足"),
    PRODUCT_UP_EXCEPTION(11008,"商品上架异常");
    private int code;
    private String msg;
    private BASICTRANCODE(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

}
