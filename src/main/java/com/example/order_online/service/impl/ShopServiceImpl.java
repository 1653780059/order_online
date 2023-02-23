package com.example.order_online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order_online.pojo.domain.Shop;
import com.example.order_online.service.ShopService;
import com.example.order_online.mapper.ShopMapper;
import org.springframework.stereotype.Service;

/**
* @author 16537
* @description 针对表【shop】的数据库操作Service实现
* @createDate 2023-02-23 09:45:54
*/
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop>
    implements ShopService{

}




