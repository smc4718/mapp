package com.pyj.mapp.service;

import com.pyj.mapp.dto.OrderDto;

public interface CompensationService {
    void distributeCompensation(OrderDto order);
}