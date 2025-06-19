package com.pyj.mapp.service;

import com.pyj.mapp.dao.PointMapper;
import com.pyj.mapp.dto.PointDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointMapper pointMapper;

    @Override
    public List<PointDto> findPointsByUserNo(int userNo) {
        return pointMapper.findByUserNo(userNo);
    }

    @Override
    public int getTotalPointByUserNo(int userNo) {
        return pointMapper.sumPointByUserNo(userNo);
    }


}
