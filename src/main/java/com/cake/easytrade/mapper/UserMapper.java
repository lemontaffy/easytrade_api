package com.cake.easytrade.mapper;

import com.cake.easytrade.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id) throws Exception;

    @Insert("INSERT INTO users (username, email, password, auth_provider, auth_id) " +
            "VALUES (#{username}, #{email}, " +
            "CASE WHEN #{authProvider} = 'local' THEN #{password} ELSE NULL END, " +
            "#{authProvider}, " +
            "CASE WHEN #{authProvider} != 'local' THEN #{authId} ELSE NULL END)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email) throws Exception;

    @Update("UPDATE users SET username = #{username}, email = #{email}, password = #{password} " +
            "WHERE id = #{id} AND auth_provider = 'local'")
    void updateLocalUser(User user);

    @Update("UPDATE users SET username = #{username}, email = #{email} " +
            "WHERE id = #{id} AND auth_provider != 'local'")
    void updateSsoUser(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteUser(Long id);
}