package com.example.order_online.init;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.order_online.constants.ElasticSearchConstant;
import com.example.order_online.mapper.GoodsMapper;
import com.example.order_online.mapper.ShopMapper;
import com.example.order_online.pojo.domain.Goods;
import com.example.order_online.pojo.domain.Shop;
import com.example.order_online.pojo.dto.GoodsDoc;
import com.example.order_online.pojo.dto.ShopDoc;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 16537
 * @Classname ElasticSearchInit
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/23 14:55
 */
@Component
@Order(0)
public class ElasticSearchInit implements CommandLineRunner {
    @Resource
    private ShopMapper shopMapper;
    @Resource
    private RestHighLevelClient client;
    @Resource
    private GoodsMapper goodsMapper;
    @Override
    public void run(String... args) throws Exception {
        BulkRequest shopRequest = new BulkRequest();
        List<Shop> list = shopMapper.getAllShopAndGoodsName();
        list.forEach(shop->{
            ShopDoc shopDoc = new ShopDoc(shop);
            shopRequest.add(new IndexRequest(ElasticSearchConstant.SHOP_INDEX_NAME).id(shop.getId().toString()).source(JSON.toJSONString(shopDoc), XContentType.JSON));
        });
        client.bulk(shopRequest, RequestOptions.DEFAULT);

        BulkRequest goodsRequest = new BulkRequest();
        List<Goods> goodsList= goodsMapper.selectList(new QueryWrapper<>());
        goodsList.forEach(shop->{
            GoodsDoc goodsDoc = new GoodsDoc(shop);
            goodsRequest.add(new IndexRequest(ElasticSearchConstant.GOODS_INDEX_NAME).id(shop.getId().toString()).source(JSON.toJSONString(goodsDoc), XContentType.JSON));
        });
        client.bulk(goodsRequest, RequestOptions.DEFAULT);
    }
}
