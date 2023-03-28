package com.example.order_online.utils;


import com.example.order_online.pojo.domain.LoginDetails;
import com.example.order_online.pojo.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;

/**
 * @Classname SecurityUtils
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/21 13:22
 * @Created by 16537
 */
public class SecurityUtils {

    private static LoginDetails getLoginDetails(){
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)
        {
            throw new RuntimeException("用户未登录");
        }
        return (LoginDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public static User getLoginUser(){
        return getLoginDetails().getUser();
    }
    public static Boolean isRoot(){
        return getLoginDetails().getRoot();
    }
    public static Set<String> getPermission(){
        return getLoginDetails().getPermission();
    }
    public static void setAuthentication(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
