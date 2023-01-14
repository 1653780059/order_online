package com.example.order_online.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 16537
 * @Classname WebConfig
 * @Description
 * @Version 1.0.0
 * @Date 2022/12/21 17:30
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //跨域访问控制
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*");
    }
}
