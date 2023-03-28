package com.example.order_online.service;

import com.example.order_online.controller.form.ESListForm;
import com.example.order_online.pojo.dto.Result;

/**
* @author 16537
* @description 针对表【shop】的数据库操作Service
* @createDate 2023-02-23 09:45:54
*/
public interface ESService {

    Result getList(ESListForm form, String index);

    Result suggest(String search, String index);
}
