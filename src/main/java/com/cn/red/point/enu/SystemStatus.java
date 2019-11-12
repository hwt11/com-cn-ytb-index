package com.cn.red.point.enu;

public enum SystemStatus {
    OK("0000","OK"),
    ERROR("1000","system error"),
    NO_ALI("0005","请先上传支付宝二维码"),
    PASSWORD_ERROR("0001","password error"),
    NO_REFEREE("0002","no referee"),
    REPEAT("0003","号码已注册"),
    NO_SIGN("0004","no signIn"),
    NO_MONEY("0101","no more money"),
    ONE_TURN_MAX("0102","ONE_TURN_MAX"),
    LOADING("0900","审核中"),
    NEED_PAY("0901","未激活")
    ;
    private String code;
    private String msg;

    SystemStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
