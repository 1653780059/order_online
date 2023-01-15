package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author 16537
 * @Classname LoginForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/1/15 10:49
 */
@Data
public class LoginForm {
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$",message = "用户名错误")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{8,18}$",message = "密码错误")
    private String password;
    @NotBlank(message = "验证码不能为空")
    private String verification;
}
