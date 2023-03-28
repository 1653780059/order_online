package com.example.order_online.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order_online.constants.RedisConstant;
import com.example.order_online.controller.form.*;
import com.example.order_online.pojo.domain.User;
import com.example.order_online.pojo.dto.Result;
import com.example.order_online.service.RoleService;
import com.example.order_online.service.ShopService;
import com.example.order_online.service.UserService;
import com.example.order_online.mapper.UserMapper;
import com.example.order_online.utils.MailUtils;
import com.example.order_online.utils.RedisUtil;
import com.example.order_online.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

    @Override
    public Result getUserInfoById(Integer id) {
        final User user = baseMapper.selectOne(new QueryWrapper<User>().select("username", "nickName", "email", "img", "phone", "balance", "address").eq("id", id));
        return Result.success(user);
    }
    @Resource
    private RedisUtil redisUtil;
    @Override
    public Result emailChange(ChangeEmailForm form) {
        String key = RedisConstant.EMAIL_VERIFICATION+form.getOldEmail();
        final String ans =(String) redisUtil.vGet(key);
        final String verification = form.getVerification().toUpperCase(Locale.ROOT);
        if (!verification.equals(ans)){
            throw new RuntimeException("验证码错误");
        }
        redisUtil.delete(key);
        final User user = new User();
        user.setEmail(form.getNewEmail());
        baseMapper.update(user,new UpdateWrapper<User>().eq("id", SecurityUtils.getLoginUser().getId()));
        return Result.success("邮箱已更新");

    }
    @Resource
    private ShopService shopService;
    @Override
    public Result getUserShop() {
        final List<Map<String, Object>> map = shopService.getUserShop(SecurityUtils.getLoginUser().getRoot());
        return Result.success(map);
    }

    @Override
    public Result getUserList(UserListForm form) {
        final Map<String, Object> params = BeanUtil.beanToMap(form);
        int start =(form.getCurrentPage()-1)*10;
        params.put("start",start);
       List<Map<String,Object>> list= baseMapper.getUserList(params);
        int total=baseMapper.getUserListCount(params);
        return Result.success(list).put("total",total);
    }

    @Override
    @Transactional
    public Result changeStatus(boolean parseBoolean, String userId) {
        final User user = baseMapper.selectOne(new QueryWrapper<User>().eq("id", userId));
        if (user.getRoot()==1){
            throw new RuntimeException("不可修改root用户状态");
        }
        user.setEnable(parseBoolean?1:0);
        final int row = baseMapper.update(user, new UpdateWrapper<User>().eq("id", userId));
        if (row!=1){
            throw new RuntimeException("更新失败");
        }
        forceLogout(Integer.valueOf(userId));
        return Result.success("更新成功");
    }

    @Override
    public Result deleteUser(String userId) {
        final User user = baseMapper.selectOne(new QueryWrapper<User>().eq("id", userId));
        if (user.getRoot()==1){
            throw new RuntimeException("root用户不可删除");
        }
        user.setDel(1);
        final int row = baseMapper.update(user, new UpdateWrapper<User>().eq("id", userId));
        if (row!=1){
            throw new RuntimeException("删除失败");
        }
        forceLogout(Integer.valueOf(userId));
        return Result.success("删除成功");
    }

    @Override
    @Transactional
    public Result updateUser(UpdateUserForm form) {
        final User user = new User();
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        user.setNickname(form.getNickname());
        user.setRoles(JSON.toJSONString(form.getRole()));
        int row = baseMapper.update(user,new UpdateWrapper<User>().eq("id",form.getId()));
        if(row!=1){
            throw new RuntimeException("更新失败");
        }
        forceLogout(Integer.valueOf(form.getId()));
        return Result.success("更新成功");
    }
    private void forceLogout(Integer userId) {
        final User user = baseMapper.selectById(userId);
        final String pattern = RedisConstant.LOGIN_USER_PRE + user.getUsername()+"*";
        final Set<String> keys = redisUtil.keys(pattern);
        redisUtil.delete(keys);
    }
    @Override
    public List<String> getUserNameByRoleId(Integer roleId) {

        return baseMapper.getUserNameByRoleId(roleId);
    }

    @Override
    public Result joinUs() {
        int count = baseMapper.getJoinUsById(SecurityUtils.getLoginUser().getId());
        if (count!=0){
            throw new RuntimeException("请等待审核,无需重复申请");
        }
        int row =baseMapper.setUserJoinUsFlag(SecurityUtils.getLoginUser().getId());
        if (row!=1){
            throw new RuntimeException("操作失败");
        }
        return Result.success("入驻申请成功等待审核");
    }

    @Resource
    private MailUtils mailUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result joinUsConfirm(JoinUsConfirmForm form) {
        final User user = baseMapper.selectById(form.getUserId());
        if (form.getAgree()==1) {
            user.setRoles("[2]");
            final int row = baseMapper.update(user, new UpdateWrapper<User>().eq("id", user.getId()));
            if(row!=1){
                throw new RuntimeException("通过操作失败");
            }
            mailUtils.sendEmail("商户入驻通知","入驻申请已通过",user.getEmail());
            final String pattern = RedisConstant.LOGIN_USER_PRE + user.getUsername()+"*";
            final Set<String> keys = redisUtil.keys(pattern);
            redisUtil.delete(keys);
            return Result.success("通过操作成功");
        }else{
            user.setRoles("[1]");
            final int row = baseMapper.update(user, new UpdateWrapper<User>().eq("id", user.getId()));
            if (row!=1){
                throw new RuntimeException("拒绝操作失败");
            }
            mailUtils.sendEmail("商户入驻通知","入驻申请未通过",user.getEmail());
            return Result.success("拒绝操作成功");
        }

    }

    @Override
    public Result joinUsConfirmList(JoinUsConfirmListForm form) {
        final Map<String, Object> param = BeanUtil.beanToMap(form);
        int start = (form.getCurrentPage()-1)*10;
        param.put("start",start);
        List<Map<String,Object>> list = baseMapper.joinUsConfirmList(param);
        int total = baseMapper.joinUsConfirmListCount(param);
        return Result.success(list).put("total",total);
    }

}




