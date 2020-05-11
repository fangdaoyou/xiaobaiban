package com.whiteboard.exception;

public class WhiteBoardException extends RuntimeException{
    private String msg;
    private int id;

    public WhiteBoardException(int id, String msg){
        super(msg);
        this.id = id;
        this.msg = msg;
    }

    public WhiteBoardException(String msg){
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
