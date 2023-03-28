package com.example.order_online.constants;

/**
 * @author 16537
 * @Classname RabbitMQConstant
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/22 21:26
 */
public class RabbitMQConstant {
    public static final String SHOP_SYNC_QUEUE_ADD_UPDATE ="sync.shop.add.update";
    public static final String SHOP_SYNC_QUEUE_DELETE ="sync.shop.delete";
    public static final String SHOP_SYNC_QUEUE_ADD_UPDATE_KEY ="sync.shop.add.update.key";
    public static final String SHOP_SYNC_EXCHANGE ="shop.sync";
    public static final String SHOP_SYNC_QUEUE_DELETE_KEY ="sync.shop.delete.key";

    public static final String GOODS_SYNC_EXCHANGE ="goods.sync";
    public static final String GOODS_SYNC_QUEUE_ADD_UPDATE ="sync.goods.add.update";
    public static final String GOODS_SYNC_QUEUE_DELETE ="sync.goods.delete";
    public static final String GOODS_SYNC_QUEUE_ADD_UPDATE_KEY ="sync.goods.add.update.key";
    public static final String GOODS_SYNC_QUEUE_DELETE_KEY ="sync.goods.delete.key";


}
