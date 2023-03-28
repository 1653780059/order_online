package com.example.order_online.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.order_online.config.WxPayConfig;
import com.example.order_online.constants.RedisConstant;
import com.example.order_online.enums.OrderStatus;
import com.example.order_online.enums.WxApiType;
import com.example.order_online.enums.WxTradeState;
import com.example.order_online.pojo.domain.Order;
import com.example.order_online.pojo.domain.Refund;
import com.example.order_online.service.OrderService;
import com.example.order_online.service.PayService;
import com.example.order_online.service.RefundService;
import com.example.order_online.socket.WebSocketService;
import com.example.order_online.utils.RedisUtil;
import com.example.order_online.utils.WxPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author 16537
 * @Classname PayStatusCheckTask
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/2 14:54
 */
@Component
@Slf4j
public class PayStatusCheckTask {
    @Resource
    private CloseableHttpClient wxPayClient;
    @Resource
    private OrderService orderService;
    @Resource
    private PayService payService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RedissonClient redissonClient;
    @Scheduled(cron = "0/30 * * * * ?")
    public void checkNoPayOrderStatus(){
        log.info("checkNoPayOrderStatus开始");
        final Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE,-5);
        final List<Order> orders = orderService.list(new QueryWrapper<Order>().eq("order_status", OrderStatus.NOTPAY.getType()));
        final Calendar createTime = Calendar.getInstance();
        final LinkedList<String> orderNo = new LinkedList<>();
        orders.forEach(order -> {
            if (orderNo.contains(order.getOrderNo())){
                return;
            }
            orderNo.add(order.getOrderNo());
            createTime.setTime(order.getCreateTime());
            if (instance.after(createTime)){
                payService.queryOrder(order.getUserId().toString(),order.getOrderNo());
                final Order one = orderService.getOne(new QueryWrapper<Order>().eq("order_no", order.getOrderNo()).eq("shop", order.getShop()));
                if (!one.getTradeState().equals(WxTradeState.SUCCESS.getType())){
                    final Order updateOrder = new Order();
                    updateOrder.setOrderStatus(OrderStatus.CLOSED.getType());
                    redisUtil.delete("no:pay:" + order.getOrderNo());
                    orderService.update(updateOrder,new UpdateWrapper<Order>().eq("order_no",order.getOrderNo()));
                }
            }
        });
        log.info("checkNoPayOrderStatus结束");
    }
    @Resource
    private RefundService refundService;
    @Scheduled(cron = "0/30 * * * * ?")
    public void checkRefundStatus(){
        final List<Order> orders = orderService.list(new QueryWrapper<Order>().eq("order_status", OrderStatus.REFUND.getType()));
        orders.forEach(order->{
            final Order updateOrder = new Order();
            final Refund refund = refundService.getOne(new QueryWrapper<Refund>().eq("order_no", order.getOrderNo()).eq("shop", order.getShop()));

            if(refund.getRefundStatus().equals(WxTradeState.SUCCESS.getType())){
                updateOrder.setOrderStatus(OrderStatus.REFUND_SUCCESS.getType());
                orderService.update(updateOrder,new UpdateWrapper<Order>().eq("order_no",order.getOrderNo()).eq("shop",order.getShop()));
            }else if(refund.getRefundStatus().equals(WxTradeState.REFUND.getType())){
                payService.queryRefund(refund.getRefundNo(),order.getOrderNo(),order.getShop());
            }

        });
    }
}
