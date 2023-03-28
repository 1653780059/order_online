package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 16537
 * @Classname OrderListForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/2 21:45
 */
@Data
public class OrderListForm {
    @Min(value = 1,message = "当前页不能小于1")
    @NotNull(message = "参数错误")
    private Integer currentPage;
}
