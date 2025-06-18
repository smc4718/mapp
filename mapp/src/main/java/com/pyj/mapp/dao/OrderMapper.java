package com.pyj.mapp.dao;

import com.pyj.mapp.dto.OrderDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {
    OrderDto findById(@Param("orderNo") Long orderNo);
}

