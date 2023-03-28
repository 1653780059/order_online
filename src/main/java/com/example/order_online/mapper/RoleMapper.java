package com.example.order_online.mapper;

import com.example.order_online.pojo.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.order_online.pojo.dto.TreeDto;

import java.util.List;
import java.util.Map;

/**
* @author 16537
* @description 针对表【role】的数据库操作Mapper
* @createDate 2023-03-22 17:35:44
* @Entity com.example.order_online.pojo.domain.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<Map<String, Object>> getRoleList(Map<String, Object> param);

    Integer getRoleListCount(Map<String, Object> param);

    List<TreeDto> getMenuTree(int parent);
}




