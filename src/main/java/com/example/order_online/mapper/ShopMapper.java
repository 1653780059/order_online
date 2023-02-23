package com.example.order_online.mapper;

import com.example.order_online.pojo.domain.Shop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 16537
* @description 针对表【shop】的数据库操作Mapper
* @createDate 2023-02-23 09:45:54
* @Entity com.example.order_online.pojo.domain.Shop
*/
public interface ShopMapper extends BaseMapper<Shop> {
    Shop getShopAndGoodsName(Integer id);
}




