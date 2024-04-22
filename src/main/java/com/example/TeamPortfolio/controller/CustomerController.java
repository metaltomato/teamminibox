package com.example.TeamPortfolio.controller;

import com.example.TeamPortfolio.dto.CustomerJoinDTO;
import com.example.TeamPortfolio.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customer")
@Log4j2
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    //로그인
    @GetMapping("/login")
    public void loginGET(String errorCode, String logout) {
        log.info("로그인 화면");
    }

    //회원가입
    @GetMapping("/join")
    public void joinGET(){
        log.info("회원가입..");
    }
    @PostMapping("/join")
    public String joinPOST(CustomerJoinDTO customerJoinDTO, RedirectAttributes redirectAttributes) {
        log.info("회원가입하기");
        log.info(customerJoinDTO);
        //회원이 아니면 가입이 되고 회원이 이미 존재하면 다시 회원가입 페이지로 이동하기
        //try:성공하면 가입이성공하고,
        try{
            customerService.join(customerJoinDTO);
            //catch: 아니라면 회원가입 창으로 다시 돌아감
        }catch (CustomerService.cidExistException e){
            redirectAttributes.addFlashAttribute("error", "cid");
            return "redirect:/customer/join";
        }
        //회원가입 후 이름 : result, 값: success로 리다이렉트속성값 설정
        redirectAttributes.addFlashAttribute("result","success");
        return "redirect:/customer/joinSuccess";
    }
    //회원가입 성공
    @GetMapping("/joinSuccess")
    public void joinSuccessGET(){log.info("회원가입성공.");}

    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

}
