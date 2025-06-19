package com.pyj.mapp.service;

import com.pyj.mapp.dto.OrderDto;

public interface OrderService {
    void processOrder(OrderDto order);
}
