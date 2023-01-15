package com.example.order_online.init;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.order_online.constants.RedisConstant;
import com.example.order_online.mapper.ConfigMapper;
import com.example.order_online.pojo.domain.Config;
import com.example.order_online.service.ConfigService;
import com.example.order_online.utils.RedisUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 16537
 * @Classname ConfigInit
 * @Description
 * @Version 1.0.0
 * @Date 2023/1/15 14:59
 */
@Component
@Order(value = 1)
public class ConfigInit implements CommandLineRunner {
    @Resource
    private ConfigMapper configMapper;
    @Resource
    private RedisUtil redisUtil;
    @Override
    public void run(String... args) throws Exception {
        List<Config> configs = configMapper.selectList(new QueryWrapper<>());
        configs.forEach(config->{
            String key = RedisConstant.SYS_CONFIG_PRE+config.getConfig();
            redisUtil.vSet(key,config.getEnable());
        });
    }
}
