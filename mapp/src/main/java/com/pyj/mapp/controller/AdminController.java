package com.pyj.mapp.controller;

import com.pyj.mapp.dto.UserDto;
import com.pyj.mapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    // 관리자 페이지 - 회원 목록
    @GetMapping("/user-management")
    public String userManagement(Model model, HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            return "redirect:/main";
        }

        List<UserDto> userList = userService.getAllUsers();
        model.addAttribute("userList", userList);
        return "admin/user-management";
    }

    // 관리자 페이지 - 추천인 조직도
    @GetMapping("/referral-tree")
    public String referralTree(HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            return "redirect:/main";
        }

        return "admin/referral-tree";
    }
}
