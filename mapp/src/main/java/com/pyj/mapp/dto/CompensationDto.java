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
public class CompensationDto {
    private Long compensationNo;   // 수당 지급 번호 (PK)
    private int receiverUserNo;    // 수당을 받은 사용자 USER_NO
    private Long orderNo;          // 어떤 주문으로부터 발생한 수당인지
    private int generation;        // 몇 대 추천인지 (예: 1대, 2대…)
    private double rate;           // 해당 세대에서 적용된 수당률 (예: 0.05)
    private int amount;            // 지급된 수당 금액
    private Date paidAt;           // 지급 일자
}
