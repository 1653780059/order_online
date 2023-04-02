package com.example.order_online.pojo.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @TableName order
 */
@TableName(value ="`order`")
@Data
@ToString
@EqualsAndHashCode
public class Order implements Serializable {
    /**
     * 
     */

    private Integer finished;
    private Integer shop;
    /**
     * 订单描述
     */
    private String title;

    /**
     * 订单Id
     */
    private String orderNo;

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 商品Id以及数量
     */
    private Object productId;

    /**
     * 订单总价
     */
    private Double totalFee;

    /**
     * 微信支付url
     */
    private String codeUrl;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 订单更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 微信平台订单号
     */
    private String transactionId;

    /**
     * 支付方式
     */
    private String tradeType;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private String tradeState;
    private Object productCount;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}