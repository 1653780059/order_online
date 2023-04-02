package com.example.order_online.pojo.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,16}$",message = "用户名错误")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{6,18}$",message = "密码错误")
    private String password;

    /**
     * 昵称
     */
    @NotNull
    private String nickname;

    /**
     * 邮箱地址
     */
    @Email
    @NotNull
    private String email;

    /**
     * 用户头像地址
     */
    @NotBlank
    private String img;

    /**
     * 用户手机号
     */
    @NotBlank
    private String phone;

    /**
     * 用户创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 用户更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 用户是否被删除，1：是，0：否
     */
    private Integer del;

    /**
     * 用户是否可用，1：是，0：否
     */
    private Integer enable;

    /**
     * 用户拥有的角色
     */
    private Object roles;

    /**
     * 是否root用户，1：是，0：否
     */
    private Integer root;
    /**
     * 收获地址
     */
    @NotBlank
    private String address;
    /**
     * 余额
     */
    private Integer balance;

    private Object shop;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}