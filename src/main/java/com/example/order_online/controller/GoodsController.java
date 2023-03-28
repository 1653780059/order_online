package com.example.order_online.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.order_online.controller.form.GoodsListForm;
import com.example.order_online.pojo.domain.Goods;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.GoodsService;
import com.example.order_online.utils.SecurityUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author 16537
 * @Classname GoodsController
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/14 12:23
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @PreAuthorize("hasAnyAuthority('root','goods:list')")
    @PostMapping("/list")
    public Result getGoodsList( @Valid @RequestBody  GoodsListForm form){
        final Integer userId = SecurityUtils.getLoginUser().getId();
        final Map<String, Object> map = BeanUtil.beanToMap(form);
        int start = (form.getCurrentPage()-1)*10;
        map.put("start",start);
        map.put("userId",userId);
        map.put("root",SecurityUtils.getLoginUser().getRoot());
        return goodsService.getGoodsList(map);
    }
    @PreAuthorize("hasAnyAuthority('root','goods:delete')")
    @DeleteMapping("/{goodsId}")
    public Result deleteGoods(@PathVariable String goodsId){
        return goodsService.deleteRecord(goodsId);
    }
    @PreAuthorize("hasAnyAuthority('root','goods:update')")
    @PostMapping("/update")
    public Result updateGoods(@RequestBody @Valid Goods goods){
        return goodsService.updateGoods(goods);
    }
    @PreAuthorize("hasAnyAuthority('root','goods:insert')")
    @PostMapping("/insert")
    public Result insertGoods(@RequestBody @Valid Goods goods){
        return goodsService.insertGoods(goods);
    }
}
