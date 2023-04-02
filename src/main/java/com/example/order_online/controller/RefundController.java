package com.example.order_online.controller;

import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.RefundService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 16537
 * @Classname RefundController
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/7 15:01
 */
@RequestMapping("/refund")
@RestController
public class RefundController {
    @Resource
    private RefundService refundService;
    @PreAuthorize("hasAnyAuthority('root','refund:list')")
    @PostMapping("/list")
    public Result getRefundListByUserId(){
        return refundService.getRefundListByUserId();
    }
    @PreAuthorize("hasAnyAuthority('root','refund:list')")
    @GetMapping("/count")
    public Result getRefundCount(){
        return refundService.getRefundCount();
    }

}
