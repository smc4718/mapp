package com.pyj.mapp.service;

import com.pyj.mapp.dao.OrderMapper;
import com.pyj.mapp.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final CompensationService compensationService;

    @Override
    @Transactional
    public void processOrder(OrderDto order) {
        // 주문 날짜 설정
        order.setOrderDate(new Date());

        // DB에 주문 저장
        orderMapper.insertOrder(order);

        // 수당 분배
        compensationService.distributeCompensation(order);
    }
}
