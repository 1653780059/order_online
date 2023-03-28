package com.example.order_online.pojo.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @TableName shop
 */
@TableName(value ="shop")
@Data
@ToString
@EqualsAndHashCode
public class Shop implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 店铺地址
     */
    private String address;

    /**
     * 1：主食:2：小吃:3：饮料
     */
    private Integer type;


    /**
     * 创建时间
     */
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill= FieldFill.INSERT_UPDATE)

    private Date updateTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 修改者
     */
    private String updateBy;
    /**
     * 商店图片
     */
    private String img;
    /**
     * 店铺拥有者
     */
    private String own;

    /**
     * 评分，5分为最高
     */
    private Double score;

    private Integer status;
    /**
     * 支付的广告费
     */
    private Double payment;
    @TableField(exist = false)
    private List<String> goodsName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}