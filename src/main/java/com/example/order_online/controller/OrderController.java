package com.example.order_online.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.order_online.controller.form.OrderFinishedConfirmForm;
import com.example.order_online.controller.form.OrderFinishedListForm;
import com.example.order_online.controller.form.OrderListForm;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.OrderService;
import com.example.order_online.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 16537
 * @Classname OrderController
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/2 13:47
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;
    @PreAuthorize("hasAnyAuthority('root','order:list')")
    @PostMapping("/list")
    public Result orderList(@RequestBody OrderListForm form){
        final Map<String, Object> stringObjectMap = BeanUtil.beanToMap(form);
        stringObjectMap.put("size",10);
        final int start = (form.getCurrentPage() - 1) * 10;
        stringObjectMap.put("start",start);
        stringObjectMap.put("userId",SecurityUtils.getLoginUser().getId());
        return orderService.orderList(stringObjectMap);
    }
    @PreAuthorize("hasAnyAuthority('root','order:finished:list')")
    @PostMapping("/finished/list")
    public Result orderFinishedList(@RequestBody OrderFinishedListForm form){
        final Map<String, Object> param = BeanUtil.beanToMap(form);
        param.put("userId", SecurityUtils.getLoginUser().getId());
        final int start = (form.getCurrentPage() - 1) * 10;
        param.put("start",start);
        return orderService.orderFinishedList(param);
    }
    @PreAuthorize("hasAnyAuthority('root','order:finished:list')")
    @GetMapping("/finished/count")
    public Result orderFinishedCount(){
        final Map<String, Object> param = new HashMap<>();
        param.put("userId", SecurityUtils.getLoginUser().getId());
        return orderService.orderFinishedCount(param);
    }
    @PreAuthorize("hasAnyAuthority('root','order:finished:confirm')")
    @PostMapping("/finished/confirm")
    public Result orderFinishedConfirm(@RequestBody @Valid OrderFinishedConfirmForm form){
        return orderService.orderFinishedConfirm(form);
    }
}
