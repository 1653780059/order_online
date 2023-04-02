package com.example.order_online.service;

import com.example.order_online.controller.form.RefundForm;
import com.example.order_online.pojo.domain.Refund;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.order_online.pojo.dto.Result;

/**
* @author 16537
* @description 针对表【refund】的数据库操作Service
* @createDate 2023-03-02 14:45:03
*/
public interface RefundService extends IService<Refund> {

    void createRefund(RefundForm form);

    Result getRefundListByUserId();

    Result getRefundCount();
}
