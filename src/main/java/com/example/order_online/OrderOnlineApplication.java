package com.example.order_online;

import cn.xuyanwu.spring.file.storage.EnableFileStorage;
import com.example.order_online.mapper.ConfigMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.annotation.Resource;

@SpringBootApplication
@EnableFileStorage
@MapperScan("com.example.order_online.mapper")
@EnableScheduling
@EnableAsync
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class OrderOnlineApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderOnlineApplication.class, args);
    }
    //rabbit消息转换
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
