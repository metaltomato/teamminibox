package com.example.TeamPortfolio.controller;

import com.example.TeamPortfolio.domain.BoxOfficeApi;
import com.example.TeamPortfolio.domain.Customer;
import com.example.TeamPortfolio.repository.BoxOfficeApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

//프로젝트 시작페이지
@Controller
public class HomeController {
    @Autowired
    private BoxOfficeApiRepository boxOfficeApiRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<BoxOfficeApi> boxOfficeList =boxOfficeApiRepository.findAll();
        model.addAttribute("boxOfficeList", boxOfficeList);
        return "index";
    }
}