package com.example.order_online.holder;

import org.springframework.security.core.Authentication;

/**
 * @author 16537
 * @Classname AuthenticationHolder
 * @Description
 * @Version 1.0.0
 * @Date 2023/1/15 17:57
 */
public class AuthenticationHolder {
    private static final ThreadLocal<Authentication> AUTHENTICATION=new ThreadLocal<>();
    public static Authentication getAuthentication(){
        return AUTHENTICATION.get();
    }
    public static void setAuthentication(Authentication authentication){
        AUTHENTICATION.set(authentication);
    }
}
