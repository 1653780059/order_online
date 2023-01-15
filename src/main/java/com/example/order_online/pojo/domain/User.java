package com.example.order_online.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
@ToString
@EqualsAndHashCode
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 用户头像地址
     */
    private String img;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户创建时间
     */
    private Date createTime;

    /**
     * 用户更新时间
     */
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}