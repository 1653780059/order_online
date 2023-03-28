package com.example.order_online.service;

import com.example.order_online.pojo.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.order_online.pojo.dto.Result;

import java.util.Map;

/**
* @author 16537
* @description 针对表【order】的数据库操作Service
* @createDate 2023-02-28 15:05:06
*/
public interface OrderService extends IService<Order> {

    Order createOrder(String total);

    Result orderList(Map<String, Object> stringObjectMap);

    int getCartOrderTotalMoney(String orderNo);
}
