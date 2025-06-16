package com.pyj.mapp.dao;

import com.pyj.mapp.dto.ReferralDto;
import com.pyj.mapp.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface ReferralMapper {
    void insertReferral(Map<String, Object> param);
    List<ReferralDto> selectByReferrer(int referrerNo);

    // 추천인 기준으로 추천받은 회원 목록 조회 (추천인 조직도용)
    List<UserDto> getReferredUsersByReferrerNo(int referrerNo);
}
