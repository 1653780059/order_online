package com.example.order_online.service.impl;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.order_online.config.WxPayConfig;
import com.example.order_online.constants.RedisConstant;
import com.example.order_online.controller.form.RefundConfirmForm;
import com.example.order_online.controller.form.RefundForm;
import com.example.order_online.controller.form.WxPayForm;
import com.example.order_online.enums.OrderStatus;
import com.example.order_online.enums.WxApiType;
import com.example.order_online.enums.WxNotifyType;
import com.example.order_online.enums.WxTradeState;
import com.example.order_online.pojo.domain.Order;
import com.example.order_online.pojo.domain.Refund;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.OrderService;
import com.example.order_online.service.PayService;
import com.example.order_online.service.RefundService;
import com.example.order_online.socket.WebSocketService;
import com.example.order_online.utils.RedisUtil;
import com.example.order_online.utils.SecurityUtils;
import com.example.order_online.utils.WxPayUtil;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.exception.ParseException;
import com.wechat.pay.contrib.apache.httpclient.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 16537
 * @Classname PayServiceImpl
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/28 12:50
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {
    @Resource
    private CloseableHttpClient wxPayClient;
    @Resource
    private WxPayConfig wxPayConfig;
    @Resource
    private RedisUtil  redisUtil;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private OrderService orderService;
    @Resource
    private Verifier verifier;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result wxNativePay(WxPayForm form) {
        String key=RedisConstant.CODE_URL+ RedisConstant.CART_PRE+ SecurityUtils.getLoginUser().getId();
        final Set<String> keys = redisUtil.keys(RedisConstant.CART_PRE + SecurityUtils.getLoginUser().getId()+"*");
        if (keys.isEmpty()){
            throw new RuntimeException("未选中商品");
        }
        Object codeUrl = redisUtil.vGet(key);
        if (!Objects.isNull(codeUrl)){
            return Result.success().put("codeUrl",codeUrl);
        }
        RLock lock = redissonClient.getLock(key + ":lock");
        lock.lock();
        try {
            if (!Objects.isNull(redisUtil.vGet(key))){
                return Result.success().put("codeUrl",redisUtil.vGet(key));
            }
            Order order = orderService.createOrder(form.getTotal());
            //地址拼接
            redisUtil.vSet("no:pay:"+order.getOrderNo(),"noPay",10, TimeUnit.MINUTES);
            String url = wxPayConfig.getDomain().concat(WxApiType.NATIVE_PAY.getType());
            HttpPost httpPost = new HttpPost(url);
            //参数拼接
            HashMap<String, Object> map = new HashMap<>();
            map.put("appid",wxPayConfig.getAppid());
            map.put("mchid",wxPayConfig.getMchId());
            map.put("description",order.getTitle());
            map.put("out_trade_no",order.getOrderNo());
            map.put("notify_url",wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
            map.put("attach",SecurityUtils.getLoginUser().getUsername()+"@"+SecurityUtils.getLoginUser().getId());
            HashMap<String, Object> amount = new HashMap<>();
            amount.put("total",Integer.parseInt(form.getTotal()));
            map.put("amount",amount);
            //参数设置
            WxPayUtil.setRequestEntity(httpPost,map);
            //发送请求，处理响应
            try(CloseableHttpResponse response = wxPayClient.execute(httpPost)){
                String bodyAsString = EntityUtils.toString(response.getEntity());
                int status = response.getStatusLine().getStatusCode();
                WxPayUtil.assertResponse(status,bodyAsString);
                HashMap hashMap = JSON.parseObject(bodyAsString, HashMap.class);
                codeUrl = hashMap.get("code_url");
                redisUtil.vSet(key,codeUrl,5,TimeUnit.MINUTES);
                return Result.success().put("codeUrl",codeUrl);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }finally {
            lock.unlock();
        }


    }
    @Resource
    WebSocketService webSocketService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String nativeNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            final String decryptText = WxPayUtil.getDecryptText(verifier, request, wxPayConfig);
            final HashMap map = JSON.parseObject(decryptText, HashMap.class);
            final String info = (String) map.get("attach");
            final String[] split = info.split("@");

            WxPayUtil.nativeNotify(split[1],map, redissonClient, redisUtil, orderService);
            WebSocketService.sendInfo("支付成功",split[0]);
        } catch (ValidationException | ParseException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
    @Resource
    private RefundService refundService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result nativeRefund(RefundForm form) {
        final Order order = orderService.getOne(new QueryWrapper<Order>().eq("order_no", form.getOrderNo()).eq("shop", form.getShop()));
        if (order==null||!order.getOrderStatus().equals(OrderStatus.SUCCESS.getType())){
            throw new RuntimeException("订单不是支付成功状态，不可以申请退款");
        }
        refundService.createRefund(form);
        return Result.success("退款申请已提交，等待商家处理");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result nativeConfirmRefund(RefundConfirmForm form) {
        final Refund refund = refundService.getOne(new QueryWrapper<Refund>().eq("refund_no", form.getRefundNo()));
        if (refund.getRefundStatus().equals(WxTradeState.SUCCESS.getType())){
            throw new RuntimeException("退款已经成功,无需重复退款");
        }
        String url = wxPayConfig.getDomain()+WxApiType.DOMESTIC_REFUNDS.getType();
        final HttpPost request = new HttpPost(url);
        final int totalMoney = orderService.getCartOrderTotalMoney(refund.getOrderNo());
        final HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no",refund.getOrderNo());
        map.put("out_refund_no",refund.getRefundNo());
        map.put("reason",refund.getReason()+"@"+refund.getShop());
        map.put("notify_url",wxPayConfig.getNotifyDomain()+WxNotifyType.REFUND_NOTIFY.getType());
        final HashMap<String, Object> amount = new HashMap<>();
        amount.put("total", totalMoney);
        amount.put("refund",form.getRefundMoney());
        map.put("amount",amount);
        amount.put("currency", "CNY");
        WxPayUtil.setRequestEntity(request,map);

        try(CloseableHttpResponse response = wxPayClient.execute(request)){
            final int statusCode = response.getStatusLine().getStatusCode();
            final String body = EntityUtils.toString(response.getEntity());
            WxPayUtil.assertResponse(statusCode,body);
            final Refund updateRefund = new Refund();
            updateRefund.setContentReturn(body);
            updateRefund.setRefundStatus(WxTradeState.REFUND.getType());
            refundService.update(updateRefund,new UpdateWrapper<Refund>().eq("refund_no",refund.getRefundNo()));
            final Order order = new Order();
            order.setOrderStatus(OrderStatus.REFUND.getType());
            orderService.update(order,new UpdateWrapper<Order>().eq("order_no",refund.getOrderNo()).eq("shop",refund.getShop()));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return Result.success("退款中");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String nativeRefundsNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            final String decryptText = WxPayUtil.getDecryptText(verifier, request, wxPayConfig);
            final HashMap map = JSON.parseObject(decryptText, HashMap.class);
            final String refundStatus = (String) map.get("refund_status");
            final Refund refund = new Refund();
            refund.setContentNotify(decryptText);
            refund.setRefundStatus(refundStatus);
            refundService.update(refund,new UpdateWrapper<Refund>().eq("refund_no",map.get("out_refund_no")));
        } catch (ValidationException | ParseException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void queryOrder(String userId, String orderNo) {
        String url = wxPayConfig.getDomain()+String.format(WxApiType.ORDER_QUERY_BY_NO.getType(), orderNo)+"?mchid="+wxPayConfig.getMchId();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", "application/json");
        try(final CloseableHttpResponse response = wxPayClient.execute(httpGet)){
            String bodyAsString = EntityUtils.toString(response.getEntity());
            log.info(bodyAsString);
            Integer status = response.getStatusLine().getStatusCode();
            WxPayUtil.assertResponse(status,bodyAsString);
            WxPayUtil.nativeNotify(userId,JSON.parseObject(bodyAsString,HashMap.class),redissonClient,redisUtil,orderService);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void queryRefund(String refundNo, String orderNo, Integer shop) {
        String url = wxPayConfig.getDomain() +String.format(WxApiType.DOMESTIC_REFUNDS_QUERY.getType(), refundNo);
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/json");
        try(final CloseableHttpResponse response = wxPayClient.execute(request)){
            final int statusCode = response.getStatusLine().getStatusCode();
            final String body = EntityUtils.toString(response.getEntity());
            WxPayUtil.assertResponse(statusCode,body);
            final HashMap map = JSON.parseObject(body, HashMap.class);
            final String status = MapUtil.getStr(map, "status");
            if (status.equals(WxTradeState.SUCCESS.getType())){
                final Refund refund = new Refund();
                refund.setContentNotify(body);
                refund.setRefundStatus(status);
                refundService.update(refund,new UpdateWrapper<Refund>().eq("refund_no",map.get("out_refund_no")));
                final Order order = new Order();
                order.setOrderStatus(OrderStatus.REFUND_SUCCESS.getType());
                orderService.update(order,new UpdateWrapper<Order>().eq("order_no",orderNo).eq("shop",shop));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }



}
