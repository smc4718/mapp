package com.pyj.mapp.dao;

import com.pyj.mapp.dto.CompensationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompensationMapper {
    void insertOne(CompensationDto compensation);
    List<CompensationDto> findByReceiverUserNo(@Param("userNo") int userNo);
}
