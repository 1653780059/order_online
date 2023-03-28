package com.example.order_online.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order_online.constants.RedisConstant;
import com.example.order_online.controller.form.PermissionUpdateForm;
import com.example.order_online.controller.form.RoleListForm;
import com.example.order_online.pojo.domain.Role;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.pojo.dto.TreeDto;
import com.example.order_online.service.RoleService;
import com.example.order_online.mapper.RoleMapper;
import com.example.order_online.service.UserService;
import com.example.order_online.utils.RedisUtil;
import com.example.order_online.utils.SecurityUtils;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author 16537
* @description 针对表【role】的数据库操作Service实现
* @createDate 2023-03-22 17:35:44
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Override
    public Result getAllRoleName() {

        final List<Role> roles = baseMapper.selectList(new QueryWrapper<Role>().select("id", "role_name").ne("id",0));
        return Result.success(roles);
    }

    @Override
    public Result getRoleList(RoleListForm form) {
        final Map<String, Object> param = BeanUtil.beanToMap(form);
        int start = (form.getCurrentPage()-1)*10;
        param.put("start",start);
        List<Map<String,Object>> list = baseMapper.getRoleList(param);
        Integer total = baseMapper.getRoleListCount(param);
        return Result.success(list).put("total",total);
    }

    @Override
    public Result getMenuTree() {
       List<TreeDto> list =  baseMapper.getMenuTree(-1);
       list.forEach(node->{
           final List<TreeDto> menuTree = baseMapper.getMenuTree(node.getId());
           if (!menuTree.isEmpty()){
               node.setChildren(menuTree);
           }
       });
       return Result.success(list);
    }
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;
    @Override
    @Transactional
    public Result permissionUpdate(PermissionUpdateForm form) {
        final Role role = baseMapper.selectById(form.getId());
        if(role.getSystemctl()==1){
            if (SecurityUtils.getLoginUser().getRoot()!=1){
                throw new RuntimeException("为了系统功能正常，不可修改系统自带角色");
            }
        }
        role.setPermissions(JSON.toJSONString(form.getPermission()).replace("\"",""));
        final int row = baseMapper.update(role, new UpdateWrapper<Role>().eq("id", role.getId()));
        if (row!=1){
            throw new RuntimeException("修改失败");
        }
        forceLogout(form.getId());
        return Result.success("修改成功");
    }

    private void forceLogout(Integer id) {
        List<String> usernames = userService.getUserNameByRoleId(id);
        usernames.forEach(username->{
            final String pattern = RedisConstant.LOGIN_USER_PRE + username+"*";
            final Set<String> keys = redisUtil.keys(pattern);
            redisUtil.delete(keys);
        });
    }

    @Override
    @Transactional
    public Result roleDelete(String roleId) {
        final Role role = baseMapper.selectById(roleId);
        if (role.getSystemctl()==1){
            if (SecurityUtils.getLoginUser().getRoot()!=1){
                throw new RuntimeException("为了系统功能正常，不可删除系统自带角色");
            }
        }
        final int row = baseMapper.deleteById(roleId);
        if (row!=1){
            throw new RuntimeException("删除失败");
        }
        forceLogout(Integer.valueOf(roleId));
        return Result.success("删除成功");
    }

    @Override
    @Transactional
    public Result roleInsert(String roleName) {
        final Role role = new Role();
        final Map map = JSON.parseObject(roleName, Map.class);
        final Integer userId = SecurityUtils.getLoginUser().getId();
        role.setRoleName((String) map.get("roleName"));
        role.setCreateBy(userId);
        role.setUpdateBy(userId);
        role.setSystemctl(0);
        role.setPermissions("[]");
        final int row = baseMapper.insert(role);
        if (row!=1) {
            throw new RuntimeException("插入失败");
        }
        return Result.success("插入成功");
    }
}




