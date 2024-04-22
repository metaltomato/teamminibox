package com.example.TeamPortfolio.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;


@Log4j2
public class Custom403Handler implements AccessDeniedHandler {
    @Override
    //HttpServletRequest:요청
    //HttpServletResponse:응답
    //AccessDeniedException:거부
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException{
        log.info("-----------서버 요청 거부------------");
        //응답코드 지정(http상태를 접근 거부 상태(403)로 응답코드 지정)
        response.setStatus(HttpStatus.FORBIDDEN.value());
        //서버에 요청하는 메세지의 header중 Content-Type값을 contentType변수에 문자열로 저장,JSON요청이 있는지 확인
        String contentType=request.getHeader("Content-Type");
        //startsWith(): 비교대상문쟈열이 입력된 문자열로 시작되는지 여부를 확인하고 true/false로 결과 return
        //startsWith("application/json")이 있으면 true, 없으면 false
        boolean jsonRequest=contentType.startsWith("application/json");
        log.info("isJSON: " + jsonRequest);
        //jsonRequest의 값이 false이면
        if(!jsonRequest){

            //지정된 링크로 돌아가라는 뜻("/member/login?error=ACCESS_DENIED") 여기로

            response.sendRedirect("/customer/login?error=ACCESS_DENIED");
        }
    }
}
