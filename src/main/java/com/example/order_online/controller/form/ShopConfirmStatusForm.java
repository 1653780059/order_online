package com.example.order_online.controller.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 16537
 * @Classname ShopConfirmStatusForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/27 15:40
 */
@Data
public class ShopConfirmStatusForm {
    @Min(value = 1,message = "id不小于1")
    @NotNull
    private Integer id;
    @Range(min = 0,max = 1,message = "参数错误")
    @NotNull
    private Integer status;
}
