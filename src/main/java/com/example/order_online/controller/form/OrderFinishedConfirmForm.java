package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 16537
 * @Classname OrderFinishedConfirmForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/4/2 16:35
 */
@Data
public class OrderFinishedConfirmForm {
    @NotBlank
    private String orderNo;
}
