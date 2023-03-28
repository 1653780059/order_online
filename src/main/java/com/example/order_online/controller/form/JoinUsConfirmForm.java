package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author 16537
 * @Classname JoinUsConfirmForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/27 16:32
 */
@Data
public class JoinUsConfirmForm {
    @NotNull
    @Min(value = 1,message = "id不小于1")
    private Integer userId;
    @Pattern(regexp = "^[10]$" ,message = "确认选项只为1,0")
    @NotNull
    private Integer agree;
}
