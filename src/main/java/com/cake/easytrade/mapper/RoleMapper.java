package com.cake.easytrade.mapper;

import com.cake.easytrade.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
public interface RoleMapper {

    @Select("SELECT id, name FROM roles WHERE id = CAST(#{roleId} AS BIGINT)")
    Role findById(Long roleId);
}
