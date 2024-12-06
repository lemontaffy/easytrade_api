package com.cake.easytrade.mapper;

import com.cake.easytrade.model.Permission;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
public interface PermissionMapper {

    @Select("SELECT ur.user_id, ur.role_id FROM user_roles ur WHERE ur.user_id = #{id}")
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "roleId", column = "role_id")
    })
    Permission findByUserId(Long id) throws Exception;

    @Insert("INSERT INTO user_roles (user_id, role_id) " +
            "VALUES (#{userId}, #{roleId})")
    void insertUserRole(Long userId, Long roleId);
}
