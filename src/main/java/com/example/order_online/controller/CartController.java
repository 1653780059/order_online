package com.example.order_online.controller;

import com.example.order_online.constants.RedisConstant;
import com.example.order_online.controller.form.CartForm;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.utils.RedisUtil;
import com.example.order_online.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 16537
 * @Classname CartController
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/27 14:46
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private RedisUtil redisUtil;
    @PreAuthorize("hasAnyAuthority('root','cart')")
    @PostMapping("/add")
    public Result goodsAdd(@RequestBody @Valid CartForm form){
        String id = SecurityUtils.getLoginUser().getId().toString();
        String key = RedisConstant.CART_PRE +id+":"+form.getShopId();
        Integer integer = (Integer) redisUtil.hGet(key, form.getGoodsId());
        String delKey=RedisConstant.CODE_URL+RedisConstant.CART_PRE+id;
        redisUtil.delete(delKey);
        if (integer==null){
            redisUtil.hSet(key,form.getGoodsId(),1);

        }else{
            redisUtil.hIncrement(key,form.getGoodsId(),1L);

        }
        if (redisUtil.hGet(key,"total")==null){
            Integer total=form.getTotal();
            redisUtil.hSet(key,"total",total);
        }else{
            Integer total=form.getTotal()+(Integer) redisUtil.hGet(key,"total");
            redisUtil.hSet(key,"total",total);
        }
        return Result.success();
    }
    @PreAuthorize("hasAnyAuthority('root','cart')")
    @PostMapping("/sub")
    public Result goodsSub(@RequestBody @Valid CartForm form){
        String id = SecurityUtils.getLoginUser().getId().toString();
        String key = RedisConstant.CART_PRE +id+":"+form.getShopId();
        Integer integer = (Integer) redisUtil.hGet(key, form.getGoodsId());
        if (integer==null){
            throw new RuntimeException("商品未选中");
        }
        integer=integer-1;
        System.out.println(redisUtil.hGet(key, "total").getClass());
        final Integer total = (Integer)redisUtil.hGet(key, "total") - form.getTotal();
        String delKey=RedisConstant.CODE_URL+RedisConstant.CART_PRE+id;
        redisUtil.delete(delKey);
        if (integer==0){
            redisUtil.hDelete(key,form.getGoodsId());
        }else{
            redisUtil.hSet(key,form.getGoodsId(),integer);
        }

        if (total<=0){
            redisUtil.hDelete(key,"total");
        }else{
            redisUtil.hSet(key,"total",total);
        }
        return Result.success();
    }
    @PreAuthorize("hasAnyAuthority('root','cart')")
    @PostMapping("/remove")
    public Result remove(){
        String id = SecurityUtils.getLoginUser().getId().toString();
        String key = RedisConstant.CART_PRE +id;
        final Set<String> keys = redisUtil.keys(key+"*");
        keys.forEach(K->{
            redisUtil.delete(K);
        });
        String delKey=RedisConstant.CODE_URL+RedisConstant.CART_PRE+id;
        redisUtil.delete(delKey);
        return Result.success();
    }
    @PreAuthorize("hasAnyAuthority('root','cart')")
    @PostMapping("/get")
    public Result getGoodsCount(@RequestBody @Valid CartForm form){
        String id = SecurityUtils.getLoginUser().getId().toString();
        String key = RedisConstant.CART_PRE +id+":"+form.getShopId();
        Integer integer = (Integer) redisUtil.hGet(key, form.getGoodsId());
        if (integer==null){
            integer=0;
        }
        return Result.success().put("count",integer);
    }
    @PreAuthorize("hasAnyAuthority('root','cart')")
    @GetMapping("/init")
    public Result initCart(){
     String keyPattern=RedisConstant.CART_PRE+SecurityUtils.getLoginUser().getId()+"*";
        final Set<String> keys = redisUtil.keys(keyPattern);
        final Map<Object, Object> result = new HashMap<>();
        AtomicInteger total = new AtomicInteger(0);
        keys.forEach(key->{
            final Map<Object, Object> entries = redisUtil.hEntries(key);
            total.updateAndGet(v->v+ ((Integer)entries.get("total")));
            result.putAll(entries);
        });
        result.put("total",total);
        return Result.success(result);
    }
}
