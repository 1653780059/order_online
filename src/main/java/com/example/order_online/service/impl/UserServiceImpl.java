package com.example.order_online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order_online.pojo.domain.User;
import com.example.order_online.service.UserService;
import com.example.order_online.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
* @author 16537
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-01-15 16:06:59
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public Set<String> getPermissions(String username) {
        return baseMapper.getPermissions(username);
    }
}




