package com.example.order_online.service;

import com.example.order_online.controller.form.*;
import com.example.order_online.pojo.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.order_online.pojo.dto.Result;

import java.util.List;
import java.util.Set;

/**
* @author 16537
* @description 针对表【user】的数据库操作Service
* @createDate 2023-01-15 16:06:59
*/
public interface UserService extends IService<User> {

    Set<String> getPermissions(String username);

    Result getUserInfoById(Integer id);

    Result emailChange(ChangeEmailForm form);

    Result getUserShop();

    Result getUserList(UserListForm form);

    Result changeStatus(boolean parseBoolean, String userId);

    Result deleteUser(String userId);

    Result updateUser(UpdateUserForm form);

    List<String> getUserNameByRoleId(Integer id);

    Result joinUs();

    Result joinUsConfirm(JoinUsConfirmForm form);

    Result joinUsConfirmList(JoinUsConfirmListForm form);
}
