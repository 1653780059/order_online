package com.example.order_online.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 
 * @TableName goods
 */
@TableName(value ="goods")
@Data
@ToString
@EqualsAndHashCode
public class Goods implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    @Min(value = 1,message = "id不小于1")
    private Integer id;

    /**
     * 商品名称
     */
    @Pattern(regexp = "[\\u4E00-\\u9FFF\\w]{1,10}",message ="商品名称应为1到10的字母汉字数字的组合" )
    @NotBlank
    private String name;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品描述
     */
    @Pattern(regexp = "[\\u4E00-\\u9FFF\\w]{1,30}",message ="商品名称应为1到30的字母汉字数字的组合" )
    @NotBlank
    private String des;

    /**
     * 商品图片
     */
    private String img;

    /**
     * 1：主食:2：小吃:3：饮料
     */

    @NotNull
    @Range(min = 1,max = 3,message = "type参数错误")
    private Integer type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
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
     * 属于哪个店铺
     */
    @Min(value = 1,message = "店铺id不小于1")
    @NotNull
    private Integer shop;
    /**
     * 点单数量
     */
    private Integer orderCount;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}