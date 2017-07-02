package com.xfj.mmail.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.schema.JsonSerializableSchema;

import java.io.Serializable;

/**
 * Created by asus on 2017/7/2.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable{
    private int status;
    private String msg;
    private T data;

    public int getStatus(){
        return  status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    private ServerResponse(int status){
        this.status = status;
    }

    private ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status,String msg,T data){
        this.data = data;
        this.msg = msg;
        this.status = status;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResopnseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse<T> getServerResponseSuccess(){
        return new ServerResponse<T>(ResopnseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> getServerResponseSuccessMessage(String msg){
        return new ServerResponse<T>(ResopnseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> getServerResponseSuccess(T date){
        return new ServerResponse<T>(ResopnseCode.SUCCESS.getCode(),date);
    }

    public static <T> ServerResponse<T> getServerResponseSuccess(String msg,T data){
        return new ServerResponse<T>(ResopnseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse<T> getServerResponseError(){
        return new ServerResponse<T>(ResopnseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> getServerResponseErrorMessage(String msg){
        return new ServerResponse<T>(ResopnseCode.ERROR.getCode(),msg);
    }

    public static <T> ServerResponse<T> getServerResponseCodeAndMessage(int code,String msg){
        return new ServerResponse<T>(code,msg);
    }

}
