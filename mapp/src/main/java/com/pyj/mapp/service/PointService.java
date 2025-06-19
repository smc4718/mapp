package com.pyj.mapp.service;

import com.pyj.mapp.dto.PointDto;

import java.util.List;

public interface PointService {
    List<PointDto> findPointsByUserNo(int userNo);
    int getTotalPointByUserNo(int userNo);
}
