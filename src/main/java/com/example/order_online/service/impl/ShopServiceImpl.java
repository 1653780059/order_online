package com.example.order_online.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order_online.constants.ElasticSearchConstant;
import com.example.order_online.constants.RabbitMQConstant;
import com.example.order_online.controller.form.JoinUsConfirmForm;
import com.example.order_online.controller.form.ShopConfirmStatusForm;
import com.example.order_online.controller.form.ShopListForm;
import com.example.order_online.mapper.GoodsMapper;
import com.example.order_online.pojo.domain.Goods;
import com.example.order_online.pojo.domain.Shop;
import com.example.order_online.pojo.dto.RabbitMessage;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.RoleService;
import com.example.order_online.service.ShopService;
import com.example.order_online.mapper.ShopMapper;
import com.example.order_online.utils.MailUtils;
import com.example.order_online.utils.SecurityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author 16537
* @description 针对表【shop】的数据库操作Service实现
* @createDate 2023-02-23 09:45:54
*/
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop>
    implements ShopService{

    @Override
    public List<Map<String,Object>> getUserShop(Integer root) {
        final Integer userId = SecurityUtils.getLoginUser().getId();
        return baseMapper.getUserShop(userId,root);
    }

    @Override
    public Result getShopList(ShopListForm form) {
        final Map<String, Object> param = BeanUtil.beanToMap(form);
        param.put("start",(form.getCurrentPage()-1)*10);
        param.put("userId",SecurityUtils.getLoginUser().getId());
        //获取状态正常的店铺信息
        param.put("root",SecurityUtils.getLoginUser().getRoot());
        List<Map<String,Object>> list=baseMapper.getShopList(param);
        Integer total = baseMapper.getShopListCount(param);
        return Result.success(list).put("total",total);
    }
@Resource
private RabbitTemplate rabbitTemplate;
    @Resource
    private GoodsMapper goodsMapper;
    @Override
    @Transactional
    public Result deleteRecord(String shopId) {
        final int row = baseMapper.delete(new QueryWrapper<Shop>().eq("id", shopId));
        if (row!=1){
            throw new RuntimeException("删除失败");
        }
        goodsMapper.delete(new QueryWrapper<Goods>().eq("shop",shopId));
        final String shop = RabbitMessage.getESMsg("shop", shopId);

        rabbitTemplate.convertAndSend(RabbitMQConstant.SHOP_SYNC_EXCHANGE,RabbitMQConstant.SHOP_SYNC_QUEUE_DELETE_KEY,shop);
        return Result.success("删除成功");
    }

    @Override
    @Transactional
    public Result updateShop(Shop shop) {
        shop.setUpdateBy(SecurityUtils.getLoginUser().getId().toString());
        final int row = baseMapper.update(shop, new UpdateWrapper<Shop>().eq("id", shop.getId()));
        if (row!=1){
            throw new RuntimeException("更新失败");
        }
        final String json = RabbitMessage.getESMsg("shop", shop.getId().toString());
        rabbitTemplate.convertAndSend(RabbitMQConstant.SHOP_SYNC_EXCHANGE,RabbitMQConstant.SHOP_SYNC_QUEUE_ADD_UPDATE_KEY,json);
        return  Result.success("更新成功");
    }

    @Override
    @Transactional
    public Result insertShop(Shop shop) {
        final String userId = SecurityUtils.getLoginUser().getId().toString();
        shop.setOwn(userId);
        shop.setScore(4d);
        shop.setPayment(0d);
        shop.setCreateBy(userId);
        shop.setUpdateBy(userId);
        shop.setStatus(0);
        final int row = baseMapper.insert(shop);
        if (row!=1){
            throw new RuntimeException("插入失败");
        }
        baseMapper.updateUserShop(userId,shop.getId());
        //TODO 需要在确认商铺申请时才同步elasticsearch
        //final String json = RabbitMessage.getESMsg("shop", shop.getId().toString());
        //rabbitTemplate.convertAndSend(RabbitMQConstant.SHOP_SYNC_EXCHANGE,RabbitMQConstant.SHOP_SYNC_QUEUE_ADD_UPDATE_KEY,json);
        return Result.success("插入成功，等待管理审核");
    }

    @Override
    public Result getShopConfirmList(ShopListForm form) {
        final Map<String, Object> param = BeanUtil.beanToMap(form);
        int start = (form.getCurrentPage()-1)*10;
        param.put("start",start);
        List<Map<String,Object>> list = baseMapper.getShopConfirmList(param);
        int total = baseMapper.getShopConfirmListCount(param);
        return Result.success(list).put("total",total);

    }

    @Override
    @Transactional
    public Result setConfirmStatus(ShopConfirmStatusForm form) {
        final int status = form.getStatus();
        final int shopId = form.getId();
        final Shop shop = new Shop();
        shop.setStatus(status);
        final int row = baseMapper.update(shop, new UpdateWrapper<Shop>().eq("id", shopId));
        if (row!=1){
            throw new RuntimeException("更新失败");
        }
        if (status==1){
            rabbitTemplate.convertAndSend(RabbitMQConstant.SHOP_SYNC_EXCHANGE,RabbitMQConstant.SHOP_SYNC_QUEUE_ADD_UPDATE_KEY,RabbitMessage.getESMsg(ElasticSearchConstant.SHOP_INDEX_NAME,form.getId().toString()));
        }
        return Result.success("更新成功");
    }

}




