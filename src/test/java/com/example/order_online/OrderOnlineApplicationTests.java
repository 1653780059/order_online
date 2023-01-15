package com.example.order_online;

import com.example.order_online.utils.RedisUtil;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.annotation.Resource;

@SpringBootTest
class OrderOnlineApplicationTests {
    @Resource
    private RedisUtil redisUtil;
    @Test
    void contextLoads() {
        redisUtil.vSet("test",1);
        Integer test = (Integer) redisUtil.vGet("test");
    }

}
