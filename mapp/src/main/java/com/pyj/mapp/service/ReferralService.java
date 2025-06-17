package com.pyj.mapp.service;

import com.pyj.mapp.dto.UserDto;

import java.util.List;

public interface ReferralService {

    // 전체 추천 관계 목록
    List<UserDto> getAllReferralUsers();

    // 추천인 조직도 + 세대 정보 포함
    List<UserDto> getReferralTreeWithGenerations();
}