package com.example.order_online.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order_online.constants.RedisConstant;
import com.example.order_online.controller.CartController;
import com.example.order_online.controller.form.OrderFinishedConfirmForm;
import com.example.order_online.enums.OrderStatus;
import com.example.order_online.enums.WxTradeState;
import com.example.order_online.mapper.GoodsMapper;
import com.example.order_online.pojo.domain.Order;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.OrderService;
import com.example.order_online.mapper.OrderMapper;
import com.example.order_online.utils.OrderNoUtil;
import com.example.order_online.utils.RedisUtil;
import com.example.order_online.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author 16537
* @description 针对表【order】的数据库操作Service实现
* @createDate 2023-02-28 15:05:06
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private OrderNoUtil orderNoUtil;
    @Resource
    private CartController cartController;
    @Transactional
    @Override
    public Order createOrder(String total) {

        final Integer id = SecurityUtils.getLoginUser().getId();
        String cartKey = RedisConstant.CART_PRE+id;
        final Set<String> keys = redisUtil.keys(cartKey + "*");
        final ArrayList<Order> orders = new ArrayList<>();
        final String orderNo = orderNoUtil.getOrderNo();
        keys.forEach(K->{
            final Order order = new Order();
            order.setOrderNo(orderNo);
            order.setUserId(id);
            order.setShop(Integer.valueOf(K.substring(K.lastIndexOf(":")+1)));
            order.setTitle("商品购买");
            final Map<Object, Object> entries = redisUtil.hEntries(K);
            order.setTotalFee(Double.valueOf(String.valueOf(entries.get("total"))));
            entries.remove("total");
            final Set<Integer> productId = entries.keySet().stream().map(item -> Integer.parseInt((String) item)).collect(Collectors.toSet());
            order.setProductId(JSON.toJSONString(productId));
            order.setProductCount(JSON.toJSONString(entries.values()));
            order.setTradeState(WxTradeState.NOTPAY.getType());
            order.setOrderStatus(OrderStatus.NOTPAY.getType());
            order.setFinished(0);
            orders.add(order);
        });
        saveBatch(orders);
        cartController.remove();
        return orders.get(0);
    }
    @Resource
    private GoodsMapper goodsMapper;
    @Override
    public Result orderList(Map<String, Object> params) {
        final List<HashMap<String, Object>> orderList = baseMapper.getOrderList(params);
        final Long count = baseMapper.getOrderListCount(params);
        return Result.success(orderList).put("total",count);
    }

    @Override
    public int getCartOrderTotalMoney(String orderNo) {
        return baseMapper.getCartOrderTotalMoney(orderNo);
    }

    @Override
    public Result orderFinishedList(Map<String, Object> param) {
        final List<HashMap<String, Object>> orderList = baseMapper.getOrderFinishedList(param);
        final Long count = baseMapper.getOrderFinishedListCount(param);
        return Result.success(orderList).put("total",count);
    }

    @Override
    public Result orderFinishedCount(Map<String, Object> param) {
        final Long count = baseMapper.orderFinishedCount(param);
        return Result.success(count);
    }

    @Override
    @Transactional
    public Result orderFinishedConfirm(OrderFinishedConfirmForm form) {
        final Map<String, Object> params = BeanUtil.beanToMap(form);

       int row= baseMapper.orderFinishedConfirm(params);
       if (row!=1){
           throw new RuntimeException("确认失败,订单状态异常请刷新重试");
       }
       return Result.success("已确认");
    }
}




