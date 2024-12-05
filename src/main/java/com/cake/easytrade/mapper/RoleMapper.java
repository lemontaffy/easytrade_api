package com.cake.easytrade.mapper;

import com.cake.easytrade.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoleMapper {

    Role findById(String roleId);
}
