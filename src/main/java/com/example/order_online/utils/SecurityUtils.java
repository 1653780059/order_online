package com.example.order_online.utils;


import com.example.order_online.pojo.domain.LoginDetails;
import com.example.order_online.pojo.domain.TbUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * @Classname SecurityUtils
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/21 13:22
 * @Created by 16537
 */
public class SecurityUtils {

    private static LoginDetails getLoginDetails(){
        return (LoginDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public static TbUser getLoginUser(){
        return getLoginDetails().getUser();
    }
    public static Boolean isRoot(){
        return getLoginDetails().getRoot();
    }
    public static List<String> getPermission(){
        return getLoginDetails().getPermission();
    }
    public static void setAuthentication(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
