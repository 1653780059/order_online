package com.example.order_online.pojo.dto;

import com.example.order_online.pojo.domain.Goods;
import com.example.order_online.pojo.domain.Shop;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 16537
 * @Classname ShopDoc
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/23 9:47
 */
@Data
public class GoodsDoc {

    /**
     *
     */
    private Integer id;

    /**
     * 商品名称，默认高亮
     */
    private String name;

    /**
     * 1：主食:2：小吃:3：饮料
     */
    private Integer type;
    /**
     * 商品价格
     */
    private Double price;
    /**
     * 商品所属商户
     */
    private Integer shop;
    /**
     * 商品销量
     */
    private Integer orderCount;


    /**
     * 自动补全字段
     */
    private List<String> auto;
    /**
     * 描述字段，默认高亮
     */
    private String desc;
    private String img;
    public GoodsDoc(Goods goods){

        this.id=goods.getId();
        this.name=goods.getName();
        this.price=goods.getPrice().doubleValue();
        this.orderCount=goods.getOrderCount();
        this.shop=goods.getShop();
        this.type=goods.getType();
        this.auto=new ArrayList<>();
        auto.add(this.name);
        this.desc=goods.getDes();
        this.img=goods.getImg();
    }
}
