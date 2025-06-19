package com.pyj.mapp.dao;

import com.pyj.mapp.dto.PointDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PointMapper {
    void insertPoint(@Param("userNo") int userNo, @Param("orderNo") Long orderNo,
                     @Param("amount") int amount, @Param("reason") String reason);

    List<PointDto> findByUserNo(@Param("userNo") int userNo);

    int sumPointByUserNo(@Param("userNo") int userNo);

}
