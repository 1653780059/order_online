package com.example.order_online.pojo.dto;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 * @Classname Result
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/20 9:59
 * @Created by 16537
 */

public class Result extends HashMap<String ,Object> {
    public static final String CODE="code";
    public static final String MSG="msg";
    public static final String DATA="data";
    public Result(){
        super();
    }

    public Result(Integer code, String msg, Object data){
        super();
        super.put(CODE,code);
        super.put(MSG,msg);
        if(data!=null){
            super.put(DATA,data);
        }
    }
    public static Result success(){
        return success("操作成功");
    }
    public static Result success(Object data){
        return success("操作成功",data);
    }
    public static Result success(String msg){
        return success(msg,null);
    }
    public static Result success(String msg,Object data){
        return new Result(HttpStatus.OK.value(), msg,data);
    }

    @Override
    public Result put(String key, Object value) {
         super.put(key, value);
         return this;
    }
    public static Result error(){
        return error("操作失败");
    }
    public static Result error(Object data){
        return error("操作失败",data);
    }
    public static Result error(String msg){
        return error(msg,null);
    }
    public static Result error(String msg,Object data){
        return new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg,data);
    }

}
