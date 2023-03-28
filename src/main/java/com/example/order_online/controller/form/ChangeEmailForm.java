package com.example.order_online.controller.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author 16537
 * @Classname ChangeEmailForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/2 12:39
 */
@Data
public class ChangeEmailForm {
    @Email(message = "邮箱格式错误")
    @NotBlank
    private String oldEmail;
    @NotBlank
    @Email(message = "邮箱格式错误")
    private String newEmail;
    @NotBlank(message = "验证码不能为空")
    private String verification;
}
