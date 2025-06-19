package com.pyj.mapp.service;

import com.pyj.mapp.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {
    //로그인 처리
    void login(HttpServletRequest request, HttpServletResponse response) throws Exception;

    //로그아웃 처리
    void logout(HttpServletRequest request, HttpServletResponse response) throws Exception;

    UserDto getUser(String id);

    //회원 가입 처리 (추천인 코드 유효성 포함)
    void registerUser(UserDto userDto);

    UserDto findById(String id);

    //회원 목록 가져오기
    List<UserDto> getAllUsers();

    UserDto getUserByUserNo(int userNo);
}
