package com.example.order_online.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.order_online.constants.RabbitMQConstant;
import com.example.order_online.mapper.ShopMapper;
import com.example.order_online.pojo.domain.Shop;
import com.example.order_online.pojo.dto.ShopDoc;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author 16537
 * @Classname RabbitMQListener
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/22 21:14
 */
@Component
@Slf4j
public class RabbitMQListener {
    @Resource
    private ShopMapper shopMapper;
    @Resource
    private RestHighLevelClient client;
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(name = RabbitMQConstant.SHOP_SYNC_EXCHANGE),value = @Queue(RabbitMQConstant.SHOP_SYNC_QUEUE_ADD_UPDATE),key = {RabbitMQConstant.SHOP_SYNC_QUEUE_ADD_UPDATE_KEY}))
    public void syncMysqlAddAndUpdate(String json){
        JSONObject jsonObject = JSON.parseObject(json);
        String id=jsonObject.get("id").toString();
        String index = jsonObject.get("index").toString();
        Shop shop =
                shopMapper.getShopAndGoodsName(Integer.valueOf(id));
        ShopDoc shopDoc = new ShopDoc(shop);

        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.source(JSON.toJSONString(shopDoc), XContentType.JSON).id(id);
        try {
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(name = RabbitMQConstant.SHOP_SYNC_EXCHANGE),value = @Queue(RabbitMQConstant.SHOP_SYNC_QUEUE_DELETE),key = {RabbitMQConstant.SHOP_SYNC_QUEUE_DELETE_KEY}))
    public void syncMysqlDelete(String json){
        JSONObject jsonObject = JSON.parseObject(json);
        String id=jsonObject.get("id").toString();
        String index = jsonObject.get("index").toString();
        DeleteRequest deleteRequest = new DeleteRequest(index);
        deleteRequest.id(id);
        try {
            client.delete(deleteRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
