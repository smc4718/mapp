package com.pyj.mapp.controller;

import com.pyj.mapp.dto.OrderDto;
import com.pyj.mapp.dto.UserDto;
import com.pyj.mapp.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    // 상품 구매 홈
    @GetMapping("/product")
    public String productPage(@RequestParam(value = "success", required = false) String success, Model model) {
        if ("true".equals(success)) {
            model.addAttribute("message", "상품 구매가 완료되었습니다. 리워드가 분배되었습니다.");
        }
        return "order/product";
    }

    // 상품 구매 처리
    @PostMapping("/purchase")
    public String purchase(@RequestParam int amount,
                           HttpSession session) {

        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/user/login.form";
        }

        OrderDto order = OrderDto.builder()
                .buyerUserNo(loginUser.getUserNo())
                .amount(amount)
                .build();

        // 주문 저장 + 보상 처리
        orderService.processOrder(order);

        return "redirect:/order/product?success=true";
    }
}