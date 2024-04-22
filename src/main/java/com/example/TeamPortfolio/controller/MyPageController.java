package com.example.TeamPortfolio.controller;

import com.example.TeamPortfolio.service.MyPageService;
import com.example.TeamPortfolio.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/my")
public class MyPageController {

    @Autowired
    private MyPageService myPageService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/Page")
    public String myPage(Model model, Principal principal) {
        if (principal != null) {
            String cid = principal.getName();

            reservationService.updateCustomerGrade(cid); // 사용자 등급 업데이트
            String userGrade = reservationService.getUserGrade(cid); // 사용자 등급 가져오기

            model.addAttribute("userGrade", userGrade);
            return "my/myPage";
        } else {
            return "redirect:/customer/login";
        }
    }
}






