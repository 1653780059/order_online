package com.example.order_online.constants;

/**
 * @author 16537
 * @Classname RedisConstant
 * @Description
 * @Version 1.0.0
 * @Date 2023/1/14 12:42
 */
public class RedisConstant {
    //登录验证码结果前缀
    public static final String VERIFICATION_LOGIN_PRE = "verification:login:";
    //登录验证码过期时间
    public static final Long VERIFICATION_LOGIN_EX = 3L;
    //登录用户信息前缀
    public static final String LOGIN_USER_PRE = "login:user:";
    //登录用户过期时间
    public static final long LOGIN_USER_EX = 30;
    //系统配置信息前缀
    public static final String SYS_CONFIG_PRE = "sys:config:";
    //用户名未找到前缀，防穿透
    public static final String USERNAME_NOTFOUND_PRE = "username:notfound:";
    //用户名未找到过期时间
    public static final long USERNAME_NOTFOUND_EX = 3;

    //登录失败次数前缀
    public static final String LOGIN_FAIL_PRE = "login:fail:";
    //登录失败次数达到上限后可重新登录时间
    public static final long LOGIN_FAIL_EX = 10;
    //登录失败上限次数
    public static final Integer LOGIN_FAIL_MAX_COUNT = 5;
    public static final String CART_PRE = "cart:";
    public static final String CODE_URL = "codeUrl:";
    public static final String EMAIL_VERIFICATION = "email:verification:";
}
