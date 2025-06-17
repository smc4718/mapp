package com.pyj.mapp.service;

import com.pyj.mapp.dao.ReferralMapper;
import com.pyj.mapp.dto.ReferralDto;
import com.pyj.mapp.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReferralServiceImpl implements ReferralService {

    private final ReferralMapper referralMapper;

    @Override
    public List<UserDto> getAllReferralUsers() {
        return referralMapper.getAllReferralUsers();
    }

    @Override
    public List<UserDto> getReferralTreeWithGenerations() {
        List<UserDto> allUsers = referralMapper.getAllReferralUsers();

        Map<Integer, UserDto> userMap = new HashMap<>();
        for (UserDto user : allUsers) {
            user.setGeneration(-1); // 초기화
            userMap.put(user.getUserNo(), user);
        }

        // referrerNo가 null이거나 userMap에 존재하지 않는 경우: 루트 노드
        List<UserDto> roots = allUsers.stream()
                .filter(user -> user.getReferredByUserNo() == null || !userMap.containsKey(user.getReferredByUserNo()))
                .collect(Collectors.toList());

        for (UserDto root : roots) {
            assignGeneration(root, 1, userMap);
        }

        return new ArrayList<>(userMap.values());
    }

    private void assignGeneration(UserDto user, int generation, Map<Integer, UserDto> userMap) {
        user.setGeneration(generation);

        for (UserDto child : userMap.values()) {
            Integer referredBy = child.getReferredByUserNo();
            if (referredBy != null && referredBy == user.getUserNo()) {
                assignGeneration(child, generation + 1, userMap);
            }
        }
    }

}
