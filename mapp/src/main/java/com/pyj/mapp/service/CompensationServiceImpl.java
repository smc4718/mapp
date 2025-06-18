package com.pyj.mapp.service;

import com.pyj.mapp.dao.CompensationMapper;
import com.pyj.mapp.dao.PointMapper;
import com.pyj.mapp.dao.ReferralMapper;
import com.pyj.mapp.dao.UserMapper;
import com.pyj.mapp.dto.CompensationDto;
import com.pyj.mapp.dto.OrderDto;
import com.pyj.mapp.dto.UserDto;
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
    private final ReferralMapper referralMapper;
    private final UserMapper userMapper;
    private final PointMapper pointMapper;

    @Override
    @Transactional
    public void distributeCompensation(OrderDto order) {
        int buyerNo = order.getBuyerUserNo();
        int amount = order.getAmount();

        // 추천 상위 유저들을 상위 1세대부터 최대 7세대까지 조회
        List<UserDto> uplines = referralMapper.getUplinesByGeneration(buyerNo);

        List<CompensationDto> compensations = new ArrayList<>();

        for (UserDto upline : uplines) {
            int generation = upline.getGeneration();
            double rate = getRateByGeneration(generation);
            if (rate <= 0.0) continue;

            int reward = (int) (amount * rate);

            CompensationDto compensation = CompensationDto.builder()
                    .receiverUserNo(upline.getUserNo())
                    .orderNo(order.getOrderNo())
                    .generation(generation)
                    .rate(rate)
                    .amount(reward)
                    .paidAt(new Date())
                    .build();

            compensations.add(compensation);

            // 지급 대상자에게 포인트 적립
            userMapper.addMappPoint(upline.getUserNo(), reward);

            // 포인트 적립 기록 저장
            pointMapper.insertPoint(
                    upline.getUserNo(),
                    order.getOrderNo(),
                    reward,
                    generation + "세대 추천 수당"
            );
        }

        // 일괄 insert
        if (!compensations.isEmpty()) {
            compensationMapper.insertAll(compensations);
        }
    }

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
}