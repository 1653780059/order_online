package com.example.order_online;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.order_online.constants.ElasticSearchConstant;
import com.example.order_online.constants.RabbitMQConstant;
import com.example.order_online.mapper.GoodsMapper;
import com.example.order_online.pojo.domain.Shop;
import com.example.order_online.pojo.dto.RabbitMessage;
import com.example.order_online.service.ShopService;
import com.example.order_online.utils.RedisUtil;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class OrderOnlineApplicationTests {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RestHighLevelClient client;
    @Resource
    private ShopService shopService;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Test
    void contextLoads() {


    }
    @Test
    void testShopIndex() throws IOException {

        rabbitTemplate.convertAndSend(RabbitMQConstant.SHOP_SYNC_EXCHANGE,RabbitMQConstant.SHOP_SYNC_QUEUE_DELETE_KEY,RabbitMessage.getESMsg(ElasticSearchConstant.SHOP_INDEX_NAME,"1"));
    }

}
