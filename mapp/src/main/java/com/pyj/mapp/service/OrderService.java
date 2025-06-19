package com.pyj.mapp.service;

import com.pyj.mapp.dto.OrderDto;

import java.util.List;

public interface OrderService {
    void processOrder(OrderDto order);
    List<OrderDto> findOrdersByUserNo(int userNo);
}
