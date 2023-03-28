package com.example.order_online.controller.form;

import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.Email;

/**
 * @author 16537
 * @Classname UpdateUserForm
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/22 19:52
 */
@Data
public class UpdateUserForm {
    @Email
    private String email;
    private String phone;
    private String nickname;
    private Integer[] role;
    private String id;
}
