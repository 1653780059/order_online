package com.example.order_online.utils;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author 16537
 * @Classname OrderNoUtil
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/28 13:49
 */
@Component
public class OrderNoUtil {
    @Resource
    private  RedisUtil redisUtil;

    public  String getOrderNo(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
        String key = "order:no:"+sdf.format(date);
        if (Objects.isNull(redisUtil.vGet(key))) {
            redisUtil.vSet(key,0);
        }
        Long increment = redisUtil.increment(key);
        long b = date.getTime() << 12 | increment;
        String orderNoPre = "ORDER_";
        return orderNoPre +b;
    }
    public  String getRefundNo(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
        String key = "order:no:"+sdf.format(date);
        if (Objects.isNull(redisUtil.vGet(key))) {
            redisUtil.vSet(key,0);
        }
        Long increment = redisUtil.increment(key);
        long b = date.getTime() << 12 | increment;
        String refundNoPre = "REFUND_";
        return refundNoPre +b;
    }

}
