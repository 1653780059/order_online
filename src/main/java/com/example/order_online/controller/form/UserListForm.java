package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author 16537
 * @Classname UserListForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/22 13:46
 */
@Data
public class UserListForm {
    @NotNull(message = "当前页不为空")
    @Min(value = 1,message = "当前页不小于1")
    private Integer currentPage;
    @Email
    private String email;
    private String phone;
    @Pattern(regexp = "^[a-zA-Z0-9]{3,16}$",message = "用户名错误")
    private String username;
}
