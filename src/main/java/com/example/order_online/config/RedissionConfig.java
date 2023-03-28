package com.example.order_online.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 16537
 * @Classname RedissionConfig
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/28 13:26
 */
@Configuration
public class RedissionConfig {
    // redission通过redissonClient对象使用 // 如果是多个redis集群，可以配置
    @Value("${redission.redisHost}")
    private String redisHost;
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson(){
        Config config = new Config();
        // 创建单例模式的配置
        config.useSingleServer().setAddress("redis://"+redisHost+":6379").setPassword("123321");
        return Redisson.create(config);
    }

}
