package com.example.order_online.filter;

import com.alibaba.fastjson2.JSONObject;
import com.example.order_online.constants.RedisConstant;
import com.example.order_online.pojo.domain.LoginDetails;
import com.example.order_online.utils.JwtUtils;
import com.example.order_online.utils.RedisUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import springfox.documentation.spi.service.contexts.SecurityContext;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 16537
 * @Classname LoginFilter
 * @Description
 * @Version 1.0.0
 * @Date 2023/1/14 12:43
 */
@Component
public class LoginFilter extends OncePerRequestFilter {
    @Resource
    private RedisUtil redisUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (Objects.nonNull(token)) {
            LoginDetails loginDetails;
            String key;
            try {
                String username = JwtUtils.parseToken(token);
                key= RedisConstant.LOGIN_USER_PRE+username+":"+token;
                JSONObject jsonObject = (JSONObject) redisUtil.vGet(key);
                 loginDetails = jsonObject.to(LoginDetails.class);
            }catch (Exception e){
                filterChain.doFilter(request,response);
                return;
            }
            if (Objects.nonNull(loginDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDetails,null,loginDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                refreshToken(key);
            }
        }
        filterChain.doFilter(request,response);
    }

    private void refreshToken(String key) {
        redisUtil.setEx(key,RedisConstant.LOGIN_USER_EX, TimeUnit.MINUTES);
    }
}
