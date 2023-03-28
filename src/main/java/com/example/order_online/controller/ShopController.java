package com.example.order_online.controller;

import com.example.order_online.controller.form.JoinUsConfirmForm;
import com.example.order_online.controller.form.ShopConfirmStatusForm;
import com.example.order_online.controller.form.ShopListForm;
import com.example.order_online.pojo.domain.Shop;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.ShopService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author 16537
 * @Classname ShopController
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/14 15:47
 */
@RestController
@RequestMapping("/shop")
public class ShopController {
    @Resource
    private ShopService shopService;
    @PreAuthorize("hasAnyAuthority('root','shop:list')")
    @PostMapping("/list")
    public Result getShopList(@RequestBody @Valid ShopListForm form){
        return shopService.getShopList(form);
    }
    @PreAuthorize("hasAnyAuthority('root','shop:delete')")
    @DeleteMapping("/{shopId}")
    public Result deleteGoods(@PathVariable String shopId){
        return shopService.deleteRecord(shopId);
    }
    @PreAuthorize("hasAnyAuthority('root','shop:update')")
    @PostMapping("/update")
    public Result updateGoods(@RequestBody @Valid Shop shop){
        return shopService.updateShop(shop);
    }
    @PreAuthorize("hasAnyAuthority('root','shop:insert')")
    @PostMapping("/insert")
    public Result insertShop(@RequestBody @Valid Shop shop){
        return shopService.insertShop(shop);
    }

    @PreAuthorize("hasAnyAuthority('root','shop:insert:confirm')")
    @PostMapping("/confirm/list")
    public Result getShopConfirmList(@RequestBody @Valid ShopListForm form){
        return shopService.getShopConfirmList(form);
    }
    @PreAuthorize("hasAnyAuthority('root','shop:insert:confirm')")
    @PostMapping("/confirm/insert")
    public Result setConfirmStatus(@RequestBody @Valid ShopConfirmStatusForm form){
        return shopService.setConfirmStatus(form);
    }




}
