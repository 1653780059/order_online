package com.example.order_online.service;

import com.example.order_online.controller.form.JoinUsConfirmForm;
import com.example.order_online.controller.form.ShopConfirmStatusForm;
import com.example.order_online.controller.form.ShopListForm;
import com.example.order_online.pojo.domain.Shop;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.order_online.pojo.dto.Result;

import java.util.List;
import java.util.Map;

/**
* @author 16537
* @description 针对表【shop】的数据库操作Service
* @createDate 2023-02-23 09:45:54
*/
public interface ShopService extends IService<Shop> {


    List<Map<String,Object>> getUserShop(Integer root);

    Result getShopList(ShopListForm form);

    Result deleteRecord(String shopId);

    Result updateShop(Shop goods);

    Result insertShop(Shop shop);

    Result getShopConfirmList(ShopListForm form);

    Result setConfirmStatus(ShopConfirmStatusForm form);


}
