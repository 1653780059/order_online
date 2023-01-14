package com.example.order_online.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname RequestUtils
 * @Description 封装Servlet请求，响应操作
 * @Version 1.0.0
 * @Date 2022/9/21 8:52
 * @Created by 16537
 */
public class ServletUtils {
    private static ServletRequestAttributes getRequestAttributes(){
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
    public static HttpServletRequest getRequest(){
        return getRequestAttributes().getRequest();
    }
    public static String getLoginUUID(){
        return getRequest().getHeader("uuid");
    }
    public static String getParam(String param){
        return getRequest().getParameter(param);
    }
    public static String getParam(String param,String defaultValue){
        String parameter = getRequest().getParameter(param);
        if(parameter==null){
            return defaultValue;
        }
        return parameter;
    }

    public static String getURL() {

        return getRequest().getRequestURL().toString();
    }

    public static String getURI() {
        return getRequest().getRequestURI();
    }

    public static String getHeader(String s) {
        return getRequest().getHeader(s);
    }
}
