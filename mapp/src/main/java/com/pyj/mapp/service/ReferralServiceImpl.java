package com.pyj.mapp.service;

import com.pyj.mapp.dao.ReferralMapper;
import com.pyj.mapp.dto.CompensationTargetDto;
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

    /**
     * 구매자의 상위 7세대 추천자를 generation = 1부터 시작하여 반환
     */
    @Override
    public List<CompensationTargetDto> getUplineTargets(int buyerUserNo) {
        List<UserDto> allUsers = referralMapper.getAllReferralUsers();
        Map<Integer, UserDto> userMap = allUsers.stream()
                .collect(Collectors.toMap(UserDto::getUserNo, u -> u));

        List<Integer> uplineUserNos = new ArrayList<>();
        Integer currentUserNo = buyerUserNo;

        // 위로 따라 올라가면서 추천인 번호를 수집
        while (true) {
            UserDto current = userMap.get(currentUserNo);
            if (current == null) break;

            Integer referrerNo = current.getReferredByUserNo();
            if (referrerNo == null || !userMap.containsKey(referrerNo)) break;

            uplineUserNos.add(referrerNo);
            currentUserNo = referrerNo;
        }

        // 가장 상위 추천자가 generation 1이 되도록 역순 부여
        List<CompensationTargetDto> targets = new ArrayList<>();
        for (int i = uplineUserNos.size() - 1; i >= 0 && targets.size() < 7; i--) {
            int generation = uplineUserNos.size() - i;
            targets.add(new CompensationTargetDto(uplineUserNos.get(i), generation));
        }

        return targets;
    }
}