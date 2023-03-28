package com.example.order_online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.order_online.constants.RedisConstant;
import com.example.order_online.holder.AuthenticationHolder;
import com.example.order_online.pojo.domain.LoginDetails;
import com.example.order_online.pojo.domain.User;
import com.example.order_online.service.UserService;
import com.example.order_online.utils.RedisUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 16537
 * @Classname UserDetailsServiceImpl
 * @Description
 * @Version 1.0.0
 * @Date 2023/1/14 12:49
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String failKey=RedisConstant.LOGIN_FAIL_PRE+username;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username).eq("del",0);
        User user = userService.getOne(queryWrapper);
        if(Objects.isNull(user)){
            redisUtil.vSet(RedisConstant.USERNAME_NOTFOUND_PRE+username,1,RedisConstant.USERNAME_NOTFOUND_EX, TimeUnit.MINUTES);
            throw new RuntimeException("用户不存在");
        }
        if (user.getEnable()==0){
            throw new RuntimeException("用户状态异常，请联系管理员");
        }
        if (Objects.nonNull(redisUtil.vGet(failKey))) {
            if((Integer)redisUtil.vGet(failKey)>=RedisConstant.LOGIN_FAIL_MAX_COUNT){
                Long expire = redisUtil.getExpire(failKey);
                throw new RuntimeException("重试次数过多"+(expire/60+1)+"分钟后重试");
            }
        }
        assertPassword(failKey,user.getPassword());
        Set<String> permissions = userService.getPermissions(username);
        LoginDetails loginDetails = new LoginDetails();
        loginDetails.setUser(user);
        loginDetails.setPermission(permissions);
        loginDetails.setRoot(user.getRoot().equals(1));
        return loginDetails;
    }
    /**
     * 密码验证和失败次数限制
     */
    private void assertPassword(String failKey,String password) {
        String loginPassword = (String)AuthenticationHolder.getAuthentication().getCredentials();
        if (!passwordEncoder.matches(loginPassword,password)){
            if(Objects.isNull(redisUtil.vGet(failKey))){
                redisUtil.vSet(failKey,1);
            }
            redisUtil.increment(failKey);
            if((Integer)redisUtil.vGet(failKey)>=RedisConstant.LOGIN_FAIL_MAX_COUNT){
                redisUtil.setEx(failKey,RedisConstant.LOGIN_FAIL_EX,TimeUnit.MINUTES);

                throw new RuntimeException("重试次数过多十分钟后重试");
            }

        }else{
            redisUtil.delete(failKey);
        }
    }
}
