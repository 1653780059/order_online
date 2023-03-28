package com.example.order_online.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.order_online.controller.form.*;
import com.example.order_online.pojo.domain.User;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.UserService;
import com.example.order_online.utils.SecurityUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author 16537
 * @Classname UserController
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/1 16:12
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @PreAuthorize("hasAnyAuthority('root','user:getById')")
    @GetMapping("/getById")
    public Result getUserInfoById(){
        final Integer id = SecurityUtils.getLoginUser().getId();
        return userService.getUserInfoById(id);
    }
    @PreAuthorize("hasAnyAuthority('root','user:update')")
    @PostMapping("/update")
    public Result userInsert(@RequestBody @Valid User user){
        userService.update(user,new UpdateWrapper<User>().eq("username",user.getUsername()));
        return Result.success("修改成功");
    }
    @PreAuthorize("hasAnyAuthority('root','user:email:update')")
    @PostMapping("/email/change")
    public Result emailChange(@RequestBody @Valid ChangeEmailForm form){

        return userService.emailChange(form);
    }
    @PreAuthorize("hasAnyAuthority('root','user:shop')")
    @GetMapping("/shop")
    public Result getShop(){
        return userService.getUserShop();
    }
    @PreAuthorize("hasAnyAuthority('root','user:list')")
    @PostMapping("/list")
    public Result getUserList(@RequestBody @Valid UserListForm form){
        return userService.getUserList(form);
    }
    @PreAuthorize("hasAnyAuthority('root','user:status')")
    @PutMapping("/status/{status}/{userId}")
    public Result changeStatus(@PathVariable String status, @PathVariable String userId){
        return userService.changeStatus(Boolean.parseBoolean(status),userId);
    }
    @PreAuthorize("hasAnyAuthority('root','user:delete')")
    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable String userId){
        return userService.deleteUser(userId);
    }
    @PreAuthorize("hasAnyAuthority('root','user:update:back')")
    @PostMapping("/update/back")
    public Result updateUser(@RequestBody @Valid UpdateUserForm form){
        return userService.updateUser(form);
    }

    @PreAuthorize("hasAnyAuthority('root','user:joinUs')")
    @PostMapping("/joinUs")
    public Result setConfirmStatus(){
        return userService.joinUs();
    }
    @PreAuthorize("hasAnyAuthority('root','shop:joinUs')")
    @PostMapping("/shop/joinUs")
    public Result joinUsConfirm(@RequestBody JoinUsConfirmForm form){
        return userService.joinUsConfirm(form);
    }
    @PreAuthorize("hasAnyAuthority('root','shop:joinUs')")
    @PostMapping("/shop/confirm/list")
    public Result joinUsConfirmList(@RequestBody JoinUsConfirmListForm form){
        return userService.joinUsConfirmList(form);
    }
}
