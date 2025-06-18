package com.pyj.mapp.service;

import com.pyj.mapp.dto.UserDto;

import java.util.List;

public interface ReferralService {

    // 전체 추천 관계 목록
    List<UserDto> getAllReferralUsers();

    // 추천인 조직도 + 세대 정보 포함
    List<UserDto> getReferralTreeWithGenerations();

    // 등급 자동 업데이트
    void updateUserGrade(int userNo);

    // 등급 갱신
    void refreshAllUserGrades();

    // 등급 계산
    String calculateGrade(int userNo);
}