package com.example.order_online.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 16537
 * @Classname ThreadPoolConfig
 * @Description
 * @Version 1.0.0
 * @Date 2022/12/23 15:48
 */
@Component
public class ThreadPoolConfig {
    @Bean("mailPool")
    public ThreadPoolExecutor threadPool(){
        return new ThreadPoolExecutor(8,12,5, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadFactoryBuilder().setDaemon(false).setNamePrefix("mail_").build());
    }
}
