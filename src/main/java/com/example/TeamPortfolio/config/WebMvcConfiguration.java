package com.example.TeamPortfolio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // 자바스크립트 파일에 대한 MIME 타입을 'text/javascript'로 설정합니다.
        configurer.mediaType("js", org.springframework.http.MediaType.parseMediaType("text/javascript"));
    }
}
