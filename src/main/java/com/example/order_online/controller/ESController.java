package com.example.order_online.controller;

import com.example.order_online.controller.form.ESListForm;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.ESService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author 16537
 * @Classname ShopController
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/23 13:16
 */
@RestController
@RequestMapping("/es")
public class ESController {
    @Resource
    private ESService esService;
    @PostMapping("/{index}/list")
    public Result shopList(@RequestBody @Valid ESListForm shopListForm, @PathVariable String index){
        return esService.getList(shopListForm,index);
    }
    @GetMapping("/{index}/suggest")
    public Result suggest(@RequestParam String search, @PathVariable String index){
        return esService.suggest(search,index);
    }
}
