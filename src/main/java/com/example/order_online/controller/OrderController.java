package com.example.order_online.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.order_online.controller.form.OrderListForm;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
        return orderService.orderList(stringObjectMap);
    }
}
