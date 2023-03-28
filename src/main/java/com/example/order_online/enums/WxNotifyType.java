package com.example.order_online.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WxNotifyType {

	/**
	 * 支付通知
	 */
	NATIVE_NOTIFY("/wx/native/notify"),

	/**
	 * 支付通知V2
	 */
	NATIVE_NOTIFY_V2("/wx/native/notify"),


	/**
	 * 退款结果通知
	 */
	REFUND_NOTIFY("/wx/refunds/notify");

	/**
	 * 类型
	 */
	private final String type;
}
