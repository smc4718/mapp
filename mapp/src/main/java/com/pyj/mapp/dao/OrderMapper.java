package com.pyj.mapp.dao;

import com.pyj.mapp.dto.OrderDto;
import com.pyj.mapp.dto.PointDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderDto findById(@Param("orderNo") Long orderNo);

    // 새 주문
    void insertOrder(OrderDto order);

    List<PointDto> findPointsByUserNo(@Param("userNo") int userNo);

}

