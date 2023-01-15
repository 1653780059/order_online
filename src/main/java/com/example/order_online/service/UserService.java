package com.example.order_online.service;

import com.example.order_online.pojo.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
* @author 16537
* @description 针对表【user】的数据库操作Service
* @createDate 2023-01-15 16:06:59
*/
public interface UserService extends IService<User> {

    Set<String> getPermissions(String username);
}
