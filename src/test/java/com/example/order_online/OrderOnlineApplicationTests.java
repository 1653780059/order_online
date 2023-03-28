package com.example.order_online;

import com.alibaba.fastjson.JSON;
import com.example.order_online.constants.ElasticSearchConstant;
import com.example.order_online.controller.form.ESListForm;
import com.example.order_online.controller.form.WxPayForm;
import com.example.order_online.mapper.ShopMapper;
import com.example.order_online.pojo.domain.Shop;
import com.example.order_online.pojo.dto.ShopDoc;
import com.example.order_online.service.OrderService;
import com.example.order_online.service.PayService;
import com.example.order_online.service.ShopService;
import com.example.order_online.task.PayStatusCheckTask;
import com.example.order_online.utils.OrderNoUtil;
import com.example.order_online.utils.RedisUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
class OrderOnlineApplicationTests {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RestHighLevelClient client;
    @Resource
    private ShopMapper shopMapper;
    @Resource
    private ShopService shopService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private OrderNoUtil orderNoUtil;
    @Test
    void contextLoads() {


    }
    @Test
    void testShopIndex() throws IOException {
        Shop shop =
                shopMapper.getShopAndGoodsNameById(Integer.valueOf("1"));
        ShopDoc shopDoc = new ShopDoc(shop);

        IndexRequest indexRequest = new IndexRequest("shop");
        indexRequest.source(JSON.toJSONString(shopDoc), XContentType.JSON).id("1");
        try {
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
    @Test
    void testShopSearch(){
        ESListForm shopListForm = new ESListForm();
        shopListForm.setCurrentPage(1);
        shopListForm.setPageSize(10);

        //Result result = shopService.suggest(search, index);
        //System.out.println(result);
    }
    @Test
    void bulkRequest() throws IOException {
        BulkRequest request = new BulkRequest();
        List<Shop> list = shopMapper.getAllShopAndGoodsName();
        System.out.println(list);
        list.forEach(shop->{
            ShopDoc shopDoc = new ShopDoc(shop);
            request.add(new IndexRequest(ElasticSearchConstant.SHOP_INDEX_NAME).id(shop.getId().toString()).source(JSON.toJSONString(shopDoc), XContentType.JSON));
        });
        client.bulk(request, RequestOptions.DEFAULT);
    }
    @Test
    void pattern(){
//        Pattern pattern = Pattern.compile("matchAll|match|range|term");
//        System.out.println(pattern.matcher("matchAll").matches());
//        System.out.println(pattern.matcher("match").matches());
//        System.out.println(pattern.matcher("range").matches());
//        System.out.println(pattern.matcher("term").matches());
//        System.out.println(pattern.matcher("ma").matches());
//        System.out.println(pattern.matcher("rm").matches());

    }
    @Resource
    private PayService payService;
    @Resource
    private OrderService orderService;
    @Test
    void wxPayTest(){
        orderService.createOrder("1");
    }
    @Resource
    private PayStatusCheckTask payStatusCheckTask;
    @Test
    void payStatusCheck(){
        payStatusCheckTask.checkNoPayOrderStatus();
    }

}
