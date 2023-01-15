package com.example.order_online.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FastByteBuffer;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.example.order_online.constants.RedisConstant;
import com.example.order_online.constants.SysConfigConstant;
import com.example.order_online.controller.form.LoginForm;
import com.example.order_online.holder.AuthenticationHolder;
import com.example.order_online.pojo.domain.LoginDetails;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.utils.JwtUtils;
import com.example.order_online.utils.RedisUtil;
import com.example.order_online.utils.ServletUtils;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 16537
 * @Classname LoginController
 * @Description
 * @Version 1.0.0
 * @Date 2023/1/15 10:45
 */
@RestController
@RequestMapping("/sys")
@Slf4j
@Api
public class LoginController {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private Producer producer;
    @Resource
    private RedisUtil redisUtil;
    @PostMapping("/login")
    public Result login(@RequestBody @Valid LoginForm loginForm){
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        String verification = loginForm.getVerification();
        String loginUUID = ServletUtils.getLoginUUID();
        String ans = String.valueOf(redisUtil.vGet(RedisConstant.VERIFICATION_LOGIN_PRE + loginUUID));
        if(Objects.nonNull(redisUtil.vGet(RedisConstant.USERNAME_NOTFOUND_PRE+username))){
            throw new RuntimeException("用户不存在");
        }
        if(ans==null){
            throw new RuntimeException("验证码已过期");
        }
        if(!ans.equals(verification)){
            throw new RuntimeException("验证码错误");
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        AuthenticationHolder.setAuthentication(usernamePasswordAuthenticationToken);
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        LoginDetails loginDetails = (LoginDetails)authenticate.getPrincipal();
        String token = JwtUtils.getToken(loginDetails.getUsername());
        redisUtil.vSet(RedisConstant.LOGIN_USER_PRE+loginDetails.getUsername()+":"+token,loginDetails,RedisConstant.LOGIN_USER_EX,TimeUnit.MINUTES);
        if(Objects.equals(redisUtil.vGet(RedisConstant.SYS_CONFIG_PRE+ SysConfigConstant.SIGNAL_LOGIN),"1")){
            Set<String> keys = redisUtil.keys(RedisConstant.LOGIN_USER_PRE + loginDetails.getUsername() + "*");
            redisUtil.delete(keys);
        }
        return Result.success().put("token",token).put("permissions",loginDetails.getPermission());
    }
    @GetMapping("/verification")
    public Result verification(){
        String text = producer.createText();
        String[] split = text.split("=");
        String uuid = UUID.randomUUID().toString();
        redisUtil.vSet(RedisConstant.VERIFICATION_LOGIN_PRE+uuid,split[1],RedisConstant.VERIFICATION_LOGIN_EX, TimeUnit.MINUTES);
        BufferedImage image = producer.createImage(split[0]);
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image,"jpg",out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success().put("uuid",uuid).put("img", Base64.encode(out.toByteArray()));
    }
    @PreAuthorize("hasAnyAuthority('root')")
    @GetMapping("/hello")
    public Result hello(){
        return Result.success("ok");
    }
}
