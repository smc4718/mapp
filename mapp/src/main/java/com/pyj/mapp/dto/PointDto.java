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
public class PointDto {
    private Long pointNo;         // 포인트 적립 번호 (PK)
    private Long userNo;           // 포인트를 받은 사용자 USER_NO
    private Long orderNo;         // 주문번호
    private int amount;           // 적립된 포인트
    private String reason;        // 적립 사유 (예: 구매 적립, 수당 지급 등)
    private Date createdAt;       // 적립 일자
}