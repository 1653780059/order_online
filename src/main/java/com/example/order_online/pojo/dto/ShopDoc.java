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
     * 店铺拥有者
     */
    private String own;


    /**
     * 评分，5分为最高
     */
    private Double score;
    /**
     * 支付的广告费
     */
    private Double payment;
    /**
     * 自动补全字段
     */
    private List<String> auto;
    private String desc;
    private String img;
    public ShopDoc(Shop shop){

        this.id=shop.getId();
        this.payment=shop.getPayment();
        this.name=shop.getName();
        this.address=shop.getAddress();
        this.own=shop.getOwn();
        this.score=shop.getScore();
        this.type=shop.getType();
        this.auto=new ArrayList<>();
        auto.addAll(shop.getGoodsName());
        auto.add(this.name);
        auto.add(this.address);
        StringBuilder stringBuilder = new StringBuilder();
        this.auto.forEach(item->{
            stringBuilder.append(item).append(",");
        });
        this.desc=stringBuilder.toString();
        this.img=shop.getImg();
    }
}
