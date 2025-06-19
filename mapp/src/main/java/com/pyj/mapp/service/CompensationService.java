package com.pyj.mapp.service;

import com.pyj.mapp.dto.CompensationDto;
import com.pyj.mapp.dto.OrderDto;

import java.util.List;

public interface CompensationService {
    void distributeCompensation(OrderDto order);
    List<CompensationDto> findByReceiverUserNo(int userNo);

}