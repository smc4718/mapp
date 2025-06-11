package com.pyj.mapp.controller;

import com.pyj.mapp.dto.UserDto;
import com.pyj.mapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    // 회원가입 폼 페이지 이동
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user/signup";
    }

    // 회원가입 처리 요청
    @PostMapping("/signup")
    public String signup(@ModelAttribute UserDto userDto, Model model) {
        try {
            userService.registerUser(userDto);
            return "redirect:/user/login.form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "user/signup";
        }
    }

    // 로그인 폼 이동
    @GetMapping("/login.form")
    public String loginForm(HttpServletRequest request, Model model) {
        return "user/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        userService.login(request, response);
        return "redirect:/main";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        userService.logout(request, response);
        return null;
    }

}
