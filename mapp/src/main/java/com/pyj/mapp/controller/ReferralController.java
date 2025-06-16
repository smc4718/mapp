package com.pyj.mapp.controller;

import com.pyj.mapp.dto.UserDto;
import com.pyj.mapp.service.ReferralService;
import jakarta.servlet.http.HttpSession;
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
    public String showReferralTreePage(Model model) {
        // 루트 회원(예: 관리자)부터 조회
        int rootReferrerNo = 1; // 예시 관리자 userNo
        model.addAttribute("rootReferrerNo", rootReferrerNo);
        return "admin/referral-tree";
    }

    // 특정 추천인의 하위 유저 목록
    @ResponseBody
    @GetMapping("/referral-tree/children")
    public List<UserDto> getChildren(@RequestParam int referrerNo) {
        return referralService.getReferredUsersByReferrerNo(referrerNo);
    }
}