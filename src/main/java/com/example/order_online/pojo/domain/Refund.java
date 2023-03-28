package com.example.order_online.pojo.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @TableName refund
 */
@TableName(value ="refund")
@Data
@EqualsAndHashCode
@ToString
public class Refund implements Serializable {
    /**
     * 
     */

    private Double totalFee;
    /**
     * 
     */
    private String orderNo;

    /**
     * 
     */
    private String refundNo;

    /**
     * 
     */
    private String refundStatus;

    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 
     */
    private String reason;

    private Object productId;
    private Integer shop;

    /**
     * 
     */
    private String contentReturn;

    /**
     * 
     */
    private String contentNotify;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}