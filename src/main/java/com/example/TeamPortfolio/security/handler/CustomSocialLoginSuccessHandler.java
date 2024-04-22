package com.example.TeamPortfolio.security.handler;

import com.example.TeamPortfolio.dto.CustomerSecurityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

    @Log4j2
    @RequiredArgsConstructor
// AuthenticationSuccessHandler :로그인 성공 후 특정 동작을 제어하기 위한 인터페이스
    public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {
        //비밀번호를 암호화 하는 객체
        private final PasswordEncoder passwordEncoder;
        @Override
        //HttpServletRequest:서버에요청
        //HttpServletResponse:서버에응답
        //Authentication:인증
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException{
            log.info("---------------------");
            log.info("카카오 로그인 핸들러 .....");
            //getPrincipal(): 로그인한 객체의 사용자 정보를 가져옴
            log.info(authentication.getPrincipal());
            CustomerSecurityDTO customerSecurityDTO=(CustomerSecurityDTO) authentication.getPrincipal();
            String encodedPw=customerSecurityDTO.getCpw();

            //소셜 로그인(카카오 로그인) 이고 회원의 비밀번호가 1111
            if(customerSecurityDTO.isSocial() && (customerSecurityDTO.getCpw().equals("1111") ||
                    passwordEncoder.matches("1111",customerSecurityDTO.getCid()))){
                log.info("비밀번호를 변경해야 합니다");
                log.info("회원 정보를 수정하여 다시 접속합니다");
                response.sendRedirect("/customer/modify");
                return;
            }else{
                response.sendRedirect("/movie/list");
            }
        }

    }

