package com.pyj.mapp.controller;

import com.pyj.mapp.dto.UserDto;
import com.pyj.mapp.service.CompensationService;
import com.pyj.mapp.service.OrderService;
import com.pyj.mapp.service.PointService;
import com.pyj.mapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rewards")
public class RewardController {

    private final CompensationService compensationService;
    private final PointService pointService;
    private final UserService userService;
    private final OrderService orderService;

    @GetMapping
    public String viewAllRewards(@SessionAttribute("user") UserDto user, Model model) {
        int userNo = user.getUserNo();

        model.addAttribute("totalPoint", pointService.getTotalPointByUserNo(userNo));  // 총 포인트
        model.addAttribute("compensations", compensationService.findByReceiverUserNo(userNo));  // 수당 내역
        model.addAttribute("points", pointService.findPointsByUserNo(userNo));  // 포인트 내역
        model.addAttribute("orderList", orderService.findOrdersByUserNo(userNo));  // 구매 내역

        return "reward/reward-summary";
    }
}