package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author 16537
 * @Classname CartAddForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/27 17:20
 */
@Data
public class CartForm {
    @NotBlank(message = "商品id不能为空")
    @Min(value = 1,message = "商品id不小于1")
    private String goodsId;
    @NotBlank(message = "店铺id不空")
    @Min(value = 1,message = "店铺id不小于1")
    private String shopId;
    @Min(value = 0,message = "total不能小于0")
    private Integer total;
}
