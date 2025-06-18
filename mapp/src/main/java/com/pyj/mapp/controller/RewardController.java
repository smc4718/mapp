package com.pyj.mapp.controller;

import com.pyj.mapp.dao.CompensationMapper;
import com.pyj.mapp.dao.PointMapper;
import com.pyj.mapp.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/rewards")
class RewardController {

    private final CompensationMapper compensationMapper;
    private final PointMapper pointMapper;

    public RewardController(CompensationMapper compensationMapper, PointMapper pointMapper) {
        this.compensationMapper = compensationMapper;
        this.pointMapper = pointMapper;
    }

    @GetMapping
    public String viewAllRewards(@SessionAttribute("loginUser") UserDto user, Model model) {
        model.addAttribute("compensations", compensationMapper.findByReceiverUserNo(user.getUserNo()));
        model.addAttribute("points", pointMapper.findByUserNo(user.getUserNo()));
        return "reward/reward-summary";
    }
}