package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 16537
 * @Classname GoodsListForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/14 12:32
 */
@Data
public class ShopListForm {
    @NotNull(message = "当前页不为空")
    @Min(value = 1,message = "当前页不小于1")
    private Integer currentPage;

}
