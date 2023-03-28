package com.example.order_online.mapper;

import com.example.order_online.pojo.domain.Shop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author 16537
* @description 针对表【shop】的数据库操作Mapper
* @createDate 2023-02-23 09:45:54
* @Entity com.example.order_online.pojo.domain.Shop
*/
public interface ShopMapper extends BaseMapper<Shop> {
    Shop getShopAndGoodsNameById(Integer id);
    List<Shop> getAllShopAndGoodsName();

    List<Map<String,Object>> getUserShop(@Param("userId") Integer userId, @Param("root")Integer root);

    List<Map<String, Object>> getShopList(Map<String, Object> param);

    Integer getShopListCount(Map<String, Object> param);

    void updateUserShop(@Param("userId") String userId, @Param("shopId") Integer shopId);

    List<Map<String, Object>> getShopConfirmList(Map<String, Object> param);

    int getShopConfirmListCount(Map<String, Object> param);
}




