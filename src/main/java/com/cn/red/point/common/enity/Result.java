package com.cn.red.point.common.enity;

import com.cn.red.point.enu.SystemStatus;

public class Result {
    public static Result OK = new Result(SystemStatus.OK.getCode(), SystemStatus.OK.getMsg(), (Object) null);
    //返回给前端的是 code 0
    public static Result ERROR = new Result(SystemStatus.ERROR.getCode(), SystemStatus.ERROR.getMsg(), (Object) null);
    public static Result NO_SIGN = new Result(SystemStatus.NO_SIGN.getCode(), SystemStatus.NO_SIGN.getMsg(), (Object) null);
    public static Result NO_ALI = new Result(SystemStatus.NO_ALI.getCode(), SystemStatus.NO_ALI.getMsg(), (Object) null);
    public static Result NO_PROHIBIT = new Result(SystemStatus.NO_PROHIBIT.getCode(), SystemStatus.NO_ALI.getMsg(), (Object) null);
    private String code;
    private String msg;
    private Object data;

    public Result() {
    }

    public Result(String code) {
        this.code = code;
    }

    public Result(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public Result setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return this.data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("code:" + this.code + ";msg:" + this.msg + ";data:" + this.data);
        return sb.toString();
    }
}