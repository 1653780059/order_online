package com.example.order_online.mapper;

import com.example.order_online.pojo.domain.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 16537
* @description 针对表【order】的数据库操作Mapper
* @createDate 2023-02-28 15:05:06
* @Entity com.example.order_online.pojo.domain.Order
*/
public interface OrderMapper extends BaseMapper<Order> {
    List<HashMap<String,Object>> getOrderList(Map param);

    int getCartOrderTotalMoney(String orderNo);

    Long getOrderListCount(Map<String, Object> params);

    List<HashMap<String, Object>> getOrderFinishedList(Map<String, Object> param);

    Long getOrderFinishedListCount(Map<String, Object> param);

    int orderFinishedConfirm(Map<String, Object> params);

    Long orderFinishedCount(Map<String, Object> param);
}




