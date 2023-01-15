package com.example.order_online;

import com.example.order_online.mapper.ConfigMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
@MapperScan("com.example.order_online.mapper")
public class OrderOnlineApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderOnlineApplication.class, args);

    }

}
