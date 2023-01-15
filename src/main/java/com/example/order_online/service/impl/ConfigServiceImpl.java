package com.example.order_online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order_online.pojo.domain.Config;
import com.example.order_online.service.ConfigService;
import com.example.order_online.mapper.ConfigMapper;
import org.springframework.stereotype.Service;

/**
* @author 16537
* @description 针对表【config】的数据库操作Service实现
* @createDate 2023-01-15 14:54:02
*/
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config>
    implements ConfigService{

}




