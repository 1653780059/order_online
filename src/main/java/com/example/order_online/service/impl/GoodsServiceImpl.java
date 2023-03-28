package com.example.order_online.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order_online.constants.RabbitMQConstant;
import com.example.order_online.pojo.domain.Goods;
import com.example.order_online.pojo.dto.RabbitMessage;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.GoodsService;
import com.example.order_online.mapper.GoodsMapper;
import com.example.order_online.utils.SecurityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 16537
* @description 针对表【goods】的数据库操作Service实现
* @createDate 2023-02-23 09:53:05
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

    @Override
    public List<String> getNameById() {

        return null;
    }

    @Override
    public Result getGoodsList(Map<String, Object> map) {
        final List<HashMap<String, Object>> goodsList = baseMapper.getGoodsList(map);
        final Integer goodsListCount = baseMapper.getGoodsListCount(map);
        return Result.success(goodsList).put("total",goodsListCount);
    }
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Override
    public Result deleteRecord(String goodsId) {
        final int id = baseMapper.delete(new QueryWrapper<Goods>().eq("id", goodsId));
        if (id!=1){
            throw new RuntimeException("删除失败");
        }
        final String json = RabbitMessage.getESMsg("goods", goodsId);
        rabbitTemplate.convertAndSend(RabbitMQConstant.GOODS_SYNC_EXCHANGE,RabbitMQConstant.GOODS_SYNC_QUEUE_DELETE_KEY,json);

        return Result.success("删除成功");
    }

    @Override
    public Result updateGoods(Goods goods) {
        final int id = baseMapper.update(goods, new UpdateWrapper<Goods>().eq("id", goods.getId()));
        if (id!=1){
            throw new RuntimeException("更新失败");
        }
        final String json = RabbitMessage.getESMsg("goods", goods.getId().toString());
        rabbitTemplate.convertAndSend(RabbitMQConstant.GOODS_SYNC_EXCHANGE,RabbitMQConstant.GOODS_SYNC_QUEUE_ADD_UPDATE_KEY,json);

        return Result.success("更新成功");
    }

    @Override
    public Result insertGoods(Goods goods) {
        final Integer userId = SecurityUtils.getLoginUser().getId();
        goods.setCreateBy(userId.toString());
        goods.setUpdateBy(userId.toString());
        final int row = baseMapper.insert(goods);
        if (row!=1){
            throw new RuntimeException("插入失败");
        }
        final String json = RabbitMessage.getESMsg("goods", goods.getId().toString());
        rabbitTemplate.convertAndSend(RabbitMQConstant.GOODS_SYNC_EXCHANGE,RabbitMQConstant.GOODS_SYNC_QUEUE_ADD_UPDATE_KEY,json);
        return Result.success("插入成功");
    }
}




