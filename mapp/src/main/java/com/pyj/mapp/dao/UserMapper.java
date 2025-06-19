package com.pyj.mapp.dao;

import com.pyj.mapp.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    UserDto getUser(Map<String, Object> map);
    void insertUser(UserDto user);
    UserDto findById(String id);
    UserDto findByReferralCode(String referralCode);
    List<UserDto> findAll();

    void addMappPoint(@Param("userNo") int userNo, @Param("amount") int amount);

    UserDto findUserByUserNo(@Param("userNo") int userNo);

}
