package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 16537
 * @Classname RefundConfirmForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/7 18:00
 */
@Data
public class RefundConfirmForm {
    @NotBlank(message = "退款单号错误")
    private String refundNo;
    @NotNull(message = "退款金额错误")
    @Min(value = 0,message = "退款金额错误")
    private Integer refundMoney;
}
