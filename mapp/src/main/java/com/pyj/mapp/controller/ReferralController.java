// ReferralController.java
package com.pyj.mapp.controller;

import com.pyj.mapp.dto.UserDto;
import com.pyj.mapp.service.ReferralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class ReferralController {

    private final ReferralService referralService;

    // 추천인 조직도 페이지
    @GetMapping("/referral-tree")
    public String showReferralTreePage() {
        return "admin/referral-tree";
    }

    // 전체 추천인 관계 기반 트리 데이터
    @ResponseBody
    @GetMapping("/referral-tree/data")
    public List<UserDto> getReferralTreeData() {
        return referralService.getReferralTreeWithGenerations();
    }

    // 등급 전체 갱신
    @PostMapping(value = "/referral-tree/refresh-grades", produces = "text/plain; charset=UTF-8")
    @ResponseBody
    public String refreshAllGrades() {
        referralService.refreshAllUserGrades();
        return "등급 갱신이 완료되었습니다.";
    }
}