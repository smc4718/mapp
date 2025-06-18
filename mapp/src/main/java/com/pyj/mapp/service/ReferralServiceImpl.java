package com.pyj.mapp.service;

import com.pyj.mapp.dao.ReferralMapper;
import com.pyj.mapp.dto.ReferralDto;
import com.pyj.mapp.dto.UserDto;
import jakarta.annotation.PostConstruct;
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

    @Override
    public void updateUserGrade(int userNo) {
        String newGrade = calculateGrade(userNo);
        referralMapper.updateUserGrade(Map.of("userNo", userNo, "grade", newGrade));
    }

    @Override
    public void refreshAllUserGrades() {
        List<UserDto> users = referralMapper.getAllReferralUsers();
        for (UserDto user : users) {
            updateUserGrade(user.getUserNo());
        }
    }

    @Override
    public String calculateGrade(int userNo) {
        List<Integer> firstGen = referralMapper.getReferredUserNos(userNo);
        int directCount = firstGen.size();

        if (directCount >= 5) {
            boolean m2Condition = checkLineCondition(firstGen, 2);
            if (m2Condition) {
                boolean m3Condition = checkLineCondition(firstGen, 3);
                if (m3Condition) {
                    boolean m4Condition = checkLineCondition(firstGen, 3);
                    if (m4Condition) {
                        boolean m5Condition = checkLineCondition(firstGen, 4);
                        if (m5Condition) {
                            boolean m6Condition = checkLineCondition(firstGen, 4);
                            if (m6Condition) {
                                boolean m7Condition = checkLineCondition(firstGen, 5);
                                if (m7Condition) return "M7";
                                return "M6";
                            }
                            return "M5";
                        }
                        return "M4";
                    }
                    return "M3";
                }
                return "M2";
            }
        }
        return "M1";
    }

    // 각 라인에 해당 조건 수 이상 있는지 확인
    private boolean checkLineCondition(List<Integer> referredList, int requiredCountPerLine) {
        int lineCount = 0;
        for (Integer subUserNo : referredList) {
            int subCount = referralMapper.countReferralsByUser(subUserNo);
            if (subCount >= requiredCountPerLine) {
                lineCount++;
            }
        }
        return lineCount >= requiredCountPerLine;
    }

}
