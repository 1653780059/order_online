package com.example.order_online.service;

import com.example.order_online.pojo.domain.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.order_online.pojo.dto.Result;

import java.util.List;
import java.util.Map;

/**
* @author 16537
* @description 针对表【goods】的数据库操作Service
* @createDate 2023-02-23 09:53:05
*/
public interface GoodsService extends IService<Goods> {

    List<String> getNameById();

    Result getGoodsList(Map<String, Object> map);

    Result deleteRecord(String goodsId);

    Result updateGoods(Goods goods);

    Result insertGoods(Goods goods);
}
