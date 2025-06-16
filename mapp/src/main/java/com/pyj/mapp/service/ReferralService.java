package com.pyj.mapp.service;

import com.pyj.mapp.dto.ReferralDto;
import com.pyj.mapp.dto.UserDto;

import java.util.List;

public interface ReferralService {

    //추천인 번호로 추천받은 사용자 목록 조회
    List<UserDto> getReferredUsersByReferrerNo(int referrerNo);
}
