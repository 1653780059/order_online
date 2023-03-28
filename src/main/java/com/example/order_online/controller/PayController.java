package com.example.order_online.controller;

import cn.hutool.json.JSONUtil;
import com.example.order_online.controller.form.RefundConfirmForm;
import com.example.order_online.controller.form.RefundForm;
import com.example.order_online.controller.form.WxPayForm;
import com.example.order_online.enums.OrderStatus;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.PayService;
import com.example.order_online.utils.HttpUtils;
import com.example.order_online.utils.WxPayUtil;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;

/**
 * @author 16537
 * @Classname PayController
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/28 12:48
 */
@RestController
@RequestMapping("/wx")
public class PayController {
    @Resource
    private PayService payService;
    @PreAuthorize("hasAnyAuthority('root','pay:doPay')")
    @PostMapping("/native/pay")
    public Result wxNativePay(@RequestBody @Valid WxPayForm form){
        return payService.wxNativePay(form);
    }

    @PostMapping("/native/notify")
    public String nativeNotify(HttpServletRequest request, HttpServletResponse response){

       return payService.nativeNotify(request,response);
    }
    @PreAuthorize("hasAnyAuthority('root','pay:refund')")
    @PostMapping("/native/refund")
    public Result nativeRefund(@RequestBody @Valid  RefundForm form){
        return payService.nativeRefund(form);
    }
    @PreAuthorize("hasAnyAuthority('root','pay:confirm:refund')")
    @PutMapping("/native/refund")
    public Result nativeConfirmRefund(@RequestBody @Valid RefundConfirmForm form){
        return payService.nativeConfirmRefund(form);
    }
    @PostMapping("/refunds/notify")
    public String nativeRefundsNotify(HttpServletRequest request,HttpServletResponse response){
       return payService.nativeRefundsNotify(request,response);
    }


}
