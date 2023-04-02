package com.example.order_online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order_online.controller.form.RefundForm;
import com.example.order_online.enums.OrderStatus;
import com.example.order_online.enums.WxTradeState;
import com.example.order_online.pojo.domain.Order;
import com.example.order_online.pojo.domain.Refund;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.OrderService;
import com.example.order_online.service.RefundService;
import com.example.order_online.mapper.RefundMapper;
import com.example.order_online.utils.OrderNoUtil;
import com.example.order_online.utils.SecurityUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
* @author 16537
* @description 针对表【refund】的数据库操作Service实现
* @createDate 2023-03-02 14:45:03
*/
@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund>
    implements RefundService{
    @Resource
    private OrderNoUtil orderNoUtil;
    @Resource
    private OrderService orderService;

    @Override
    public void createRefund(RefundForm form, RedissonClient redissonClient) {
        final String refundNo = orderNoUtil.getRefundNo();
        final RLock lock = redissonClient.getLock("createRefund:" + refundNo);
        lock.lock();
        try{
            final Long count = baseMapper.selectCount(new QueryWrapper<Refund>().eq("order_no", form.getOrderNo()).eq("shop", form.getShop()));
            if(count!=0){
                throw new RuntimeException("退款请求已提交");
            }
            final Refund refund = new Refund();
            refund.setOrderNo(form.getOrderNo());
            refund.setRefundNo(refundNo);
            refund.setReason(form.getReason());
            refund.setRefundStatus(WxTradeState.REFUNDING.getType());
            final Order order = orderService.getOne(new QueryWrapper<Order>().eq("order_no", form.getOrderNo()).eq("shop",form.getShop()).select("product_id","total_fee"));
            refund.setProductId(order.getProductId());
            refund.setShop(form.getShop());
            refund.setTotalFee(order.getTotalFee());
            final Order updateOrder = new Order();
            updateOrder.setOrderStatus(OrderStatus.REFUND_PROCESSING.getType());
            orderService.update(updateOrder,new UpdateWrapper<Order>().eq("order_no", form.getOrderNo()).eq("shop",form.getShop()));
            baseMapper.insert(refund);
        }finally {
            lock.unlock();
        }

    }

    @Override
    public Result getRefundListByUserId() {
        final List<HashMap<String, Object>> list = baseMapper.getRefundByUserId(SecurityUtils.getLoginUser().getId(),SecurityUtils.getLoginUser().getRoot());

        final Long count = baseMapper.getRefundByUserIdCount(SecurityUtils.getLoginUser().getId(),SecurityUtils.getLoginUser().getRoot());
        return Result.success(list).put("total",count);
    }

    @Override
    public Result getRefundCount() {
        final Long count = baseMapper.getRefundByUserIdCount(SecurityUtils.getLoginUser().getId(), SecurityUtils.getLoginUser().getRoot());
        return Result.success(count);
    }
}




