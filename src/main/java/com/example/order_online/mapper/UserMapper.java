package com.example.order_online.mapper;

import com.example.order_online.pojo.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author 16537
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-01-15 16:06:59
* @Entity com.example.order_online.pojo.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    Set<String> getPermissions(@Param("username") String username);

    List<Map<String, Object>> getUserList(Map<String, Object> params);

    int getUserListCount(Map<String, Object> params);

    List<String> getUserNameByRoleId(Integer roleId);

    int setUserJoinUsFlag(Integer userId);

    int getJoinUsById(Integer userId);

    List<Map<String, Object>> joinUsConfirmList(Map<String, Object> param);

    int joinUsConfirmListCount(Map<String, Object> param);
}




