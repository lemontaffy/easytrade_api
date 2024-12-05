package com.cake.easytrade.mapper;

import com.cake.easytrade.model.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PermissionMapper {
    Permission findByUserId(Long id) throws Exception;
}
