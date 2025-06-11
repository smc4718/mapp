package com.pyj.mapp.dao;

import com.pyj.mapp.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserMapper {
    UserDto getUser(Map<String, Object> map);

}
