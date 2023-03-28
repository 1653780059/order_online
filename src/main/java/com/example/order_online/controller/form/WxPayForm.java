package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 16537
 * @Classname WxPayForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/28 12:56
 */
@Data
public class WxPayForm {

    @Min(value = 0,message = "总价不能低于0")
    @NotNull(message = "参数错误")
    private String total;
}
