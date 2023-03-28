package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author 16537
 * @Classname RefundForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/2 14:39
 */
@Data
public class RefundForm {
    @NotBlank(message = "订单号错误")
    private String orderNo;
    @NotBlank(message = "原因必填")
    @Pattern(regexp = "^[\\u4E00-\\u9FFF\\w]{1,30}$",message = "原因必填，小于30字符")
    private String reason;
    @NotNull(message = "店铺id错误")
    @Min(value = 1,message = "店铺id不小于1")
    private Integer shop;
}
