package com.pyj.mapp.service;

import com.pyj.mapp.dao.CompensationMapper;
import com.pyj.mapp.dao.PointMapper;
import com.pyj.mapp.dao.UserMapper;
import com.pyj.mapp.dto.CompensationDto;
import com.pyj.mapp.dto.CompensationTargetDto;
import com.pyj.mapp.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompensationServiceImpl implements CompensationService {

    private final CompensationMapper compensationMapper;
    private final ReferralService referralService;
    private final UserMapper userMapper;
    private final PointMapper pointMapper;

    //구매 시 추천인 수당 분배 처리 (최대 7세대까지)
    @Override
    @Transactional
    public void distributeCompensation(OrderDto order) {
        int buyerNo = order.getBuyerUserNo();
        int amount = order.getAmount();

        // 상위 7세대까지 추천인 조회 (정확한 세대별로 CompensationTargetDto 에 담김)
        List<CompensationTargetDto> uplines = referralService.getUplineTargets(buyerNo);

        List<CompensationDto> compensations = new ArrayList<>();

        for (CompensationTargetDto upline : uplines) {
            int generation = upline.getGeneration();
            double rate = getRateByGeneration(generation);
            if (rate <= 0.0) continue;

            int reward = (int) (amount * rate);

            // 수당 엔티티 생성
            CompensationDto compensation = CompensationDto.builder()
                    .receiverUserNo(upline.getUserNo())
                    .orderNo(order.getOrderNo())
                    .generation(generation)
                    .rate(rate)
                    .amount(reward)
                    .paidAt(new Date())
                    .build();

            compensations.add(compensation);

            // 수당만큼 포인트 적립
            userMapper.addMappPoint(upline.getUserNo(), reward);

            // 포인트 적립 기록 저장
            pointMapper.insertPoint(
                    upline.getUserNo(),
                    order.getOrderNo(),
                    reward,
                    generation + "세대 추천 수당"
            );
        }

        // 수당 내역 일괄 저장
        if (!compensations.isEmpty()) {
            for (CompensationDto c : compensations) {
                compensationMapper.insertOne(c);
            }
        }
    }

    //세대별 수당 비율 정의
    private double getRateByGeneration(int generation) {
        return switch (generation) {
            case 1 -> 0.05;
            case 2 -> 0.04;
            case 3 -> 0.03;
            case 4 -> 0.02;
            case 5 -> 0.015;
            case 6 -> 0.01;
            case 7 -> 0.005;
            default -> 0.0;
        };
    }

    //특정 유저가 받은 모든 수당 내역 조회
    @Override
    public List<CompensationDto> findByReceiverUserNo(int userNo) {
        return compensationMapper.findByReceiverUserNo(userNo);
    }
}