package com.example.order_online;

import com.example.order_online.utils.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 16537
 * @Classname JwtUtilsTest
 * @Description
 * @Version 1.0.0
 * @Date 2023/1/15 14:27
 */
@SpringBootTest
public class JwtUtilsTest {
    @Test
    public void testGetToken(){
        String token = JwtUtils.getToken("123");
        String token1 = JwtUtils.getToken("123");
        Assertions.assertNotEquals(token,token1);
    }
}
