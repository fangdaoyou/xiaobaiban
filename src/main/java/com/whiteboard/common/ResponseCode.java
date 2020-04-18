package com.whiteboard.common;

public enum  ResponseCode {
    USERNAME_EMPTY(1, "用户名不能为空"),
    PASSWORD_EMPTY(2, "密码不能为空"),
    USERNAME_NOT_EXISTS(3, "用户不存在"),
    PASSWORD_ERROR(4, "密码错误"),
    PARAM_EMPTY(5,"参数不能为空"),
    EMAIL_EMPTY(6,"邮箱不能为空"),
    PHONE_EMPTY(7,"电话不能为空"),
    NICKNAME_EMPTY(8,"昵称不能为空"),
    USERNAME_EXISTS(9,"用户名已存在"),
    EMAIL_EXISTS(10,"邮箱已存在"),
    PHONE_EXISTS(11,"电话已存在"),
    REGISTER_ERROR(12,"注册失败")
    ;
    private int code;
    private String msg;

    ResponseCode(int code ,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
