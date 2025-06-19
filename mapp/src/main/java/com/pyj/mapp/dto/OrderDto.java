package com.pyj.mapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long orderNo;          // 주문 번호 (PK)
    private int buyerUserNo;      // 구매자 USER_NO
    private int amount;           // 구매 금액
    private Date orderDatetime;       // 구매 일자
}