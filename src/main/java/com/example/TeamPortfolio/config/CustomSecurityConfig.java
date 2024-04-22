package com.example.TeamPortfolio.config;

import com.example.TeamPortfolio.security.CustomUserDetailsService;
import com.example.TeamPortfolio.security.handler.Custom403Handler;
import com.example.TeamPortfolio.security.handler.CustomSocialLoginSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.io.IOException;


@Log4j2
//config:환경설정
//스프링에서 설정 클래스임, Bean을 수동으로 등록하기 위해서 설정함.
@Configuration
//final이 붙거나 @NotNull이 붙은 필드의 생성자를 자동 생성해주는 Lombok 어노테이션
@RequiredArgsConstructor
//권한설정
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {
    private final DataSource dataSource;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }
    //@Bean: 수동으로 Bean 등록
    @Bean
    //SecurityFilterChain: 스프링 부트에서 지원하는 기본 로그인 화면으로 접속하지 못하게 함(filter:거른다)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.info("******configure******");
        //http.formLogin():사용자 인증하는 메서드(사용자가 인증하는 사이트로 접속하겠음)/얜 오류나도 실행가능
        http.formLogin().loginPage("/customer/login");

        //csrf 토큰 비활성화(아이디와 비밀번호 사용하여 로그인)
        //csrf토큰은 매번 value 값이 바뀌고, hidden 으로 포함되어 들어가기 때문에 공격자 입장에서는 고정된 쿼리문만 전송해서는
        //더이상 명령이 작동하지않고 매번바뀌는 csrf토큰값을 그때그때 찍어서 맞춰야 사실상 완전방어가 됩니다.
        http.csrf().disable().authorizeRequests().requestMatchers("/reservation/start").authenticated();

        //rememberMe():자동로그인
        http.rememberMe()
                //key(): 인증받은 사용자의 정보로 token생성, key값은 임의로 설정함.(token이란 서버가 각각의 사용자를 구별하도록 고유한 정보를 담은 암호화 데이터)
                .key("12345678")
                //tokenRepository():rememberMe의 토큰 저장소
                //serDetailsService():유저의 정보를 가졍는 인터페이스
                //tokenValiditySeconds(): 생성된 token의 만료시간(30일)
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(customUserDetailsService)
                .tokenValiditySeconds(60*60*24*30);
        //exceptionHandling(): Error발생시 후처리 설정
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        //로그아웃
        http.logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                //.logoutUrl("/member/logout") // 로그아웃 처리 URL, 원칙적으로 post 방식만 지원
                .logoutSuccessUrl("/customer/logout")
                .deleteCookies("JSESSIONID", "remember-me");
        return http.build();
    }

    @Bean
    //WebSecurityCustomizer : 적용하지 않을 리소스를 설정 =
    //requestMatchers는 값을 불러오는 메소드인데, atCommonLocations()로 toStaticResources()(정적)링크를 불러온다.
    //ignoring():로 그 값을 무시한다는 코드
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("******* web configure ********");
        //toStaticResources():정적 리소스를
        //ignoring():무시해달라
        //(web) -> web.ignoring() 전부 WebSecurityCustomizer에서 지원함
        return (web) -> web.ignoring().requestMatchers(
                PathRequest.toStaticResources().atCommonLocations());
    }
    //PasswordEncoder:비밀번호 암호화(스프링지원)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //자동로그인을 위한 환경설정
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        //쿠키정보를 테이블로 저장하도록 설정
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }
    //정식 사용자가 아닌데 게시글 수정하려고 접근했을 때 서버에서 접근 거부하면 예외처리함
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new Custom403Handler();
    }

    //인증된 사용자의 cid(email)값 가져오기
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}