package com.cake.easytrade.mapper;

import com.cake.easytrade.model.User;
import com.cake.easytrade.model.UserProfileMulti;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE id = #{id}")
    Optional<User> findById(Long id);

    @Insert("INSERT INTO users (username, email, password, auth_provider, auth_id) " +
            "VALUES (#{username}, #{email}, " +
            "CASE WHEN #{authProvider} = 'local' THEN #{password} ELSE NULL END, " +
            "#{authProvider}, " +
            "CASE WHEN #{authProvider} != 'local' THEN #{authId} ELSE NULL END)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    @Insert("INSERT INTO user_profile_multi (user_id, photo_url, nickname, is_active) " +
            "VALUES (#{userId}, #{photoUrl}, #{nickname}, #{isActive})")
    void insertProfile(UserProfileMulti userProfile);

    @Update("UPDATE user_profile_multi SET photo_url = #{photoUrl}, nickname = #{nickname}, " +
            "is_active = #{isActive} WHERE id = #{profileId}")
    void updateProfile(UserProfileMulti userProfile);

    @Select("SELECT * FROM user_profile_multi WHERE user_id = #{userId}")
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "photoUrl", column = "photo_url"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "active", column = "is_active")
    })
    List<UserProfileMulti> getUserProfiles(@Param("userId") Long userId);

    @Select("SELECT * FROM user_profile_multi WHERE user_id = #{userId} AND is_active = #{active}")
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "photoUrl", column = "photo_url"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "active", column = "is_active")
    })
    UserProfileMulti getActiveProfile(@Param("userId") Long userId, boolean active);

    @Update("UPDATE user_profile_multi SET is_active = FALSE WHERE user_id = #{userId}")
    void deactivateProfiles(@Param("userId") Long userId);

    @Update("UPDATE user_profile_multi SET is_active = TRUE WHERE id = #{profileId}")
    void setActiveProfile(@Param("profileId") Long profileId);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email) throws Exception;

    @Update("UPDATE users " +
            "SET email = #{email}, " +
            "password = #{password}, " +
            "activeProfile = #{activeProfile}, " +
            "linked_auth_id = #{linkedAuthId}, " +
            "phone_number = #{phoneNumber}, " +
            "address = #{address}, " +
            "personal_custom_code = #{personalCustomCode}, " +
            "WHERE id = #{id} AND auth_provider = 'local'")
    void updateLocalUser(User user);

    @Update("UPDATE users " +
            "SET email = #{email}, " +
            "activeProfile = #{activeProfile}, " +
            "auth_id = #{authId}, " +
            "phone_number = #{phoneNumber}, " +
            "address = #{address}, " +
            "personal_custom_code = #{personalCustomCode}, " +
            "WHERE id = #{id} AND auth_provider != 'local'")
    void updateSsoUser(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteUser(Long id);

    User findByLinkedAuth(String linkedAuthId, String authProvider);
}