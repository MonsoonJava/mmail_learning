package com.xfj.mmail.common;

/**
 * Created by asus on 2017/7/2.
 */
public enum ResopnseCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(-1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    ILLEGAL_ARGUMENTS(2,"ILLEGAL_ARGUMENTS");

    private final int code;
    private final String desc;

    ResopnseCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return this.code;
    }

    public String getDesc(){
        return this.desc;
    }

}
