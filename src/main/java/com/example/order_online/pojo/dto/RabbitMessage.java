package com.example.order_online.pojo.dto;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

/**
 * @author 16537
 * @Classname RabbitMessage
 * @Description
 * @Version 1.0.0
 * @Date 2023/2/23 12:26
 */
@AllArgsConstructor
@Data
public final class RabbitMessage {

    public static String getESMsg(String index,String id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("index",index);
        map.put("id",id);
        return JSON.toJSONString(map);

    }
}
