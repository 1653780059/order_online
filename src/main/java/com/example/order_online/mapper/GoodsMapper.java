package com.example.order_online.mapper;

import com.example.order_online.pojo.domain.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 16537
* @description 针对表【goods】的数据库操作Mapper
* @createDate 2023-02-23 09:53:05
* @Entity com.example.order_online.pojo.domain.Goods
*/
public interface GoodsMapper extends BaseMapper<Goods> {
    List<HashMap<String,Object>> getGoodsList(Map<String, Object> map);
    Integer getGoodsListCount(Map<String, Object> map);
}




