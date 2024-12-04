package com.cake.easytrade.mapper;

import com.cake.easytrade.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    User findById(Long id) throws Exception;

    void insertUser(User user) throws Exception;

    User findByEmail(String email) throws Exception;
}
