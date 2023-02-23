package com.example.order_online.pojo.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.order_online.pojo.domain.Shop;
import com.example.order_online.service.GoodsService;
import lombok.Data;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 16537
 * @Classname ShopDoc
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/23 9:47
 */
@Data
public class ShopDoc {

    /**
     *
     */
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
     * 提供的商品
     */
    private Object provide;

    /**
     * 店铺拥有者
     */
    private String own;

    /**
     * 店铺vip客户
     */
    private Object vip;

    /**
     * vip折扣例如 9.5:九点五折
     */
    private Double vipDiscount;

    /**
     * 评分，5分为最高
     */
    private Double score;
    /**
     * 自动补全字段
     */
    private List<String> auto;
    private String goodsName;
    public ShopDoc(Shop shop){

        this.id=shop.getId();
        this.name=shop.getName();
        this.address=shop.getAddress();
        this.own=shop.getOwn();
        this.provide= shop.getProvide();
        this.vip=shop.getVip();
        this.vipDiscount=shop.getVipDiscount();
        this.score=shop.getScore();
        this.type=shop.getType();
        this.auto=new ArrayList<>();
        auto.addAll(shop.getGoodsName());
        auto.add(this.name);
        auto.add(this.address);
        this.goodsName=shop.getGoodsName().toString();
    }
}
