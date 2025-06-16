package com.pyj.mapp.service;

import com.pyj.mapp.dao.ReferralMapper;
import com.pyj.mapp.dto.ReferralDto;
import com.pyj.mapp.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReferralServiceImpl implements ReferralService {

    private final ReferralMapper referralMapper;

    @Override
    public List<UserDto> getReferredUsersByReferrerNo(int referrerNo) {
        return referralMapper.getReferredUsersByReferrerNo(referrerNo);
    }

}
