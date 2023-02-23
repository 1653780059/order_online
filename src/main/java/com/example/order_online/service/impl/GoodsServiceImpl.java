package com.example.order_online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order_online.pojo.domain.Goods;
import com.example.order_online.service.GoodsService;
import com.example.order_online.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 16537
* @description 针对表【goods】的数据库操作Service实现
* @createDate 2023-02-23 09:53:05
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

    @Override
    public List<String> getNameById() {

        return null;
    }
}




