package com.example.order_online.mapper;

import com.example.order_online.pojo.domain.Refund;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
* @author 16537
* @description 针对表【refund】的数据库操作Mapper
* @createDate 2023-03-02 14:45:03
* @Entity com.example.order_online.pojo.domain.Refund
*/
public interface RefundMapper extends BaseMapper<Refund> {
    List<HashMap<String,Object>> getRefundByUserId(@Param("userId") Integer userId, @Param("root") Integer root);

    Long getRefundByUserIdCount(@Param("userId") Integer userId, @Param("root") Integer root);
}




