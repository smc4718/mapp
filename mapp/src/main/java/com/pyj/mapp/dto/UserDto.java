package com.pyj.mapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    private int userNo;                 // 사용자 고유 번호 (PK)
    private String id;                  // 로그인 ID
    private String pw;                  // 로그인 비밀번호 (암호화됨)
    private String name;                // 이름
    private String gender;              // 성별 (예: M/F)
    private String email;               // 이메일
    private String hp;                  // 전화번호
    private String referralCode;        // 사용자의 추천 코드 (자신의 코드)
    private String referredByCode;      // 추천인이 입력한 추천 코드
    private String role;                // 역할 (USER, ADMIN)
    private Date joinedAt;              // 가입일
    private Integer referredByUserNo;   // 내가 누구에게 추천받았는지, DB에 없는 컬럼, but 쿼리로 조인해서 넣어줌
    private int generation;             // 몇 대인지 (1대, 2대…)
    private String grade;               // 직급 등급 (M1~M7)
    private int mappPoint;              // 수당 MAPP포인트

}
