package com.example.order_online.service;

import com.example.order_online.controller.form.RefundConfirmForm;
import com.example.order_online.controller.form.RefundForm;
import com.example.order_online.controller.form.WxPayForm;
import com.example.order_online.pojo.dto.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 16537
 * @Classname PayService
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/28 12:50
 */
public interface PayService {
    Result wxNativePay(WxPayForm form);

    String nativeNotify(HttpServletRequest request, HttpServletResponse response);

    Result nativeRefund(RefundForm form);

    Result nativeConfirmRefund(RefundConfirmForm form);

    String nativeRefundsNotify(HttpServletRequest request, HttpServletResponse response);

    void queryOrder(String userId, String orderNo);

    void queryRefund(String refundNo, String orderNo, Integer shop);


}
