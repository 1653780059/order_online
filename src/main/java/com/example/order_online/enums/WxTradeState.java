package com.example.order_online.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WxTradeState {

    /**
     * 支付成功
     */
    SUCCESS("SUCCESS"),

    /**
     * 未支付
     */
    NOTPAY("NOTPAY"),

    /**
     * 已关闭
     */
    CLOSED("CLOSED"),

    /**
     * 转入退款
     */
    REFUND("REFUND"),

    REFUNDING("REFUNDING");
    /**
     * 类型
     */
    private final String type;
}
