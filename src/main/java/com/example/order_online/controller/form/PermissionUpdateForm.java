package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 16537
 * @Classname PermissionUpdateForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/26 14:39
 */
@Data
public class PermissionUpdateForm {
    @NotBlank
    private String permission;
    @Min(value = 1,message = "id不小于1")
    @NotNull
    private Integer id;
}
