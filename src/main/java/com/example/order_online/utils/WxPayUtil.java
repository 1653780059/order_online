package com.example.order_online.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.order_online.config.WxPayConfig;
import com.example.order_online.constants.RedisConstant;
import com.example.order_online.enums.OrderStatus;
import com.example.order_online.enums.WxTradeState;
import com.example.order_online.pojo.domain.Order;
import com.example.order_online.service.OrderService;
import com.example.order_online.service.PayService;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.exception.ParseException;
import com.wechat.pay.contrib.apache.httpclient.exception.ValidationException;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 16537
 * @Classname WxPayUtil
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/28 15:42
 */
public class WxPayUtil {
    public static String getDecryptText(Verifier verifier, HttpServletRequest request, WxPayConfig wxPayConfig) throws ValidationException, ParseException {
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String signature = request.getHeader("Wechatpay-Signature");
        String body = HttpUtils.readData(request);
        String nonce = request.getHeader("Wechatpay-Nonce");
        String wechatPaySerial=request.getHeader("Wechatpay-Serial");
        NotificationRequest notificationRequest = new NotificationRequest.Builder()
                .withSerialNumber(wechatPaySerial)
                .withBody(body)
                .withNonce(nonce)
                .withTimestamp(timestamp)
                .withSignature(signature)
                .build();
        NotificationHandler handler = new NotificationHandler(verifier,wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        Notification parse = handler.parse(notificationRequest);
        if(parse!=null){
            return parse.getDecryptData();
        }else {
            return "";
        }
    }
    public static void  setRequestEntity(HttpPost httpPost, HashMap<String,Object> map){
        StringEntity entity = new StringEntity(JSON.toJSONString(map), "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
    }
    public static void assertResponse(Integer status,String bodyAsString){
        if (status == 200) {
            //处理成功

        } else if (status == 204) {
            //处理成功但是无返回值

        } else {
            //失败
            throw new RuntimeException("request failed");
        }
    }

    public static void nativeNotify(String id,HashMap map, RedissonClient redissonClient, RedisUtil redisUtil, OrderService orderService) {
        final Object transactionId = map.get("transaction_id");
        final Object tradeType = map.get("trade_type");
        final String tradeState = (String) map.get("trade_state");
        final String outTradeNo = (String) map.get("out_trade_no");
        final RLock lock = redissonClient.getLock("no:pay:" + outTradeNo + ":lock");
        lock.lock();
        try {
            if(Objects.isNull(redisUtil.vGet("no:pay:" + outTradeNo))){
                return ;
            }
            if(tradeState.equals(WxTradeState.SUCCESS.getType())){
                final Order order = new Order();
                order.setContent(JSON.toJSONString(map));
                order.setTradeType(tradeType.toString());
                order.setTransactionId(transactionId.toString());
                order.setTradeState(tradeState);
                order.setOrderStatus(OrderStatus.SUCCESS.getType());
                orderService.update(order,new UpdateWrapper<Order>().eq("order_no",outTradeNo));
                redisUtil.delete("no:pay:" + outTradeNo);
                redisUtil.delete(RedisConstant.CODE_URL+RedisConstant.CART_PRE+id);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
