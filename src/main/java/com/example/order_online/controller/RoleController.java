package com.example.order_online.controller;

import com.example.order_online.controller.form.PermissionUpdateForm;
import com.example.order_online.controller.form.RoleListForm;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author 16537
 * @Classname RoleController
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/22 17:34
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @PreAuthorize("hasAnyAuthority('root','role:getAllRoleName')")
    @GetMapping("/getAllRoleName")
    public Result getAllRoleName(){
        return roleService.getAllRoleName();
    }

    @PreAuthorize("hasAnyAuthority('root','role:list')")
    @PostMapping("/list")
    public Result getRoleList(@RequestBody @Valid  RoleListForm form){
        return roleService.getRoleList(form);
    }

    @PreAuthorize("hasAnyAuthority('root','menu:tree')")
    @GetMapping("/menu/tree")
    public Result getMenuTree(){
        return roleService.getMenuTree();
    }

    @PreAuthorize("hasAnyAuthority('root','permission:update')")
    @PostMapping("/update")
    public Result permissionUpdate(@RequestBody @Valid PermissionUpdateForm form){
        return roleService.permissionUpdate(form);
    }

    @PreAuthorize("hasAnyAuthority('root','role:delete')")
    @DeleteMapping("/{roleId}")
    public Result roleDelete(@PathVariable String roleId){
        return roleService.roleDelete(roleId);
    }

    @PreAuthorize("hasAnyAuthority('root','role:insert')")
    @PostMapping("/insert")
    public Result roleInsert(@RequestBody String roleName){
        return roleService.roleInsert(roleName);
    }

}
