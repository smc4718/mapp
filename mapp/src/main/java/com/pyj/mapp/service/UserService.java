package com.pyj.mapp.service;

import com.pyj.mapp.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void login(HttpServletRequest request, HttpServletResponse response) throws Exception;
    void logout(HttpServletRequest request, HttpServletResponse response) throws Exception;
    UserDto getUser(String id);
}
