package com.pyj.mapp.dao;

import com.pyj.mapp.dto.ReferralDto;
import com.pyj.mapp.dto.UserDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReferralMapper {
    void insertReferral(Map<String, Object> param);

    // 전체 추천 관계 기반으로 모든 유저 목록 조회 (트리용)
    List<UserDto> getAllReferralUsers();

    List<Integer> getReferredUserNos(int referrerNo);
    int countReferralsByUser(int referrerNo);
    String getUserGrade(int userNo);
    void updateUserGrade(Map<String, Object> param);

    List<UserDto> getUplinesByGeneration(@Param("userNo") int userNo);
}
