package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 16537
 * @Classname JoinUsConfirmListForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/27 17:01
 */
@Data
public class JoinUsConfirmListForm {
    @Min(value = 1,message = "当前页不能小于1")
    @NotNull(message = "参数错误")
    private Integer currentPage;
}
