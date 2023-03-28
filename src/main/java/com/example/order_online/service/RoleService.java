package com.example.order_online.service;

import com.example.order_online.controller.form.PermissionUpdateForm;
import com.example.order_online.controller.form.RoleListForm;
import com.example.order_online.pojo.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.order_online.pojo.dto.Result;

/**
* @author 16537
* @description 针对表【role】的数据库操作Service
* @createDate 2023-03-22 17:35:44
*/
public interface RoleService extends IService<Role> {

    Result getAllRoleName();

    Result getRoleList(RoleListForm form);

    Result getMenuTree();

    Result permissionUpdate(PermissionUpdateForm form);

    Result roleDelete(String roleId);

    Result roleInsert(String roleName);
}
