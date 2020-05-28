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
    REGISTER_ERROR(12,"注册失败"),
    NEED_LOGIN(13,"未登录"),
    UPDATE_ERROR(14,"更新失败"),
    TEAM_NAME_EMPTY(15,"团队名不能为空"),
    TEAM_NAME_EXISTS(16,"团队名已存在"),
    TEAM_MEMBER_COUNT_EMPTY(17,"团队人数不能为空"),
    USER_IS_IN_TEAM(18,"用户已有团队"),
    TEAM_NOT_EXISTS(19,"团队不存在"),
    PARAM_ERROR(20,"非法参数"),
    JOIN_TEAM_ERROR(21,"加入团队失败"),
    PRIVILEGE_ERROR(22,"权限错误"),
    DISBAND_ERROR(22,"解散失败"),
    TOPIC_EMPTY(23,"主题为空"),
    DESCRIPTION_EMPTY(24,"描述为空"),
    DDL_EMPTY(25,"截止时间为空"),
    PRIORITY_EMPTY(26,"紧急程度为空"),
    TASK_NOT_EXISTS(27,"任务不存在"),
    START_TIME_EMPTY(28,"开始时间为空"),
    AREA_EMPTY(29,"范围为空"),
    CLOCK_NOT_EXISTS(30,"设置不存在"),
    DISTANCE_ERROR(31,"不在范围内"),
    LATITUDE_EMPTY(32,"经度为空"),
    LONGITUDE_EMPTY(33,"纬度为空"),
    CLOCK_ID_EMPTY(34,"打卡ID为空"),
    CLOCK_IN_EXISTS(35,"重复签到"),
    CLOCK_IN_TIME_OUT(36,"签到超时"),
    CLOCK_IN_NOT_START(37,"签到未开始"),
    TYPE_EMPTY(38,"类型为空"),
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
