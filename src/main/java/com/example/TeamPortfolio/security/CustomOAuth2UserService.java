package com.example.TeamPortfolio.security;

import com.example.TeamPortfolio.domain.Customer;
import com.example.TeamPortfolio.domain.CustomerRole;
import com.example.TeamPortfolio.dto.CustomerSecurityDTO;
import com.example.TeamPortfolio.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    //OAuth2UserRequest: access token같은 정보들이 들어있는 객체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        log.info("사용자 요청!");
        log.info(userRequest);
        log.info("oauth2 사용자 ***********");
        //ClientRegistration: Oauth2제공자에 등록된 클라이언트 정보를 나타내는 클래스임.
        ClientRegistration clientRegistration=userRequest.getClientRegistration();
        //사용자 이름을 가져와서 clientName에 저장
        String clientName=clientRegistration.getClientName();
        log.info("이름: "+clientName);
        //OAuth2User: 로그인 정보(타입객체)
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //oAuth2User에서 얻어온 속성정보(attribute)를 Map배열형태로 paramMap에 저장
        Map<String, Object> paramMap=oAuth2User.getAttributes();
//        paramMap.forEach((k,v)->{
//            log.info("-------------------------");
//            log.info(k+":"+v); //k:key, v:value
//        });
        //이메일 처리
        String email=null;
        switch (clientName){
            case "kakao":
                email=getKakaoEmail(paramMap);
                break;
        }
        log.info("==============================");
        log.info(email);
        log.info("==============================");
        return generatedDTO(email, paramMap);
    }
    //카카오서비스에서 얻어온 이메일을 이용해 같은 인메일을 가진 사용자가 없으면 자동으로 회원가입하고
    //MemberSecurityDTO를 반환함.
    private CustomerSecurityDTO generatedDTO(String email, Map<String, Object> params){
        //데이터베이스에서  이메일을 가져와서 result에 저장
        Optional<Customer> result=customerRepository.findByEmail(email);
        //데이터베이스에 해당 이메일을 사용하는 사용자가 없다면(회원이 아니라면)
        if(result.isEmpty()){
            //회원추가, 여기서는 cid는 email, 비밀번호는 1111
            Customer customer=Customer.builder()
                    .cid(email)
                    .cpw(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .build();
            //새로운 회원에게 USER 권한 추가
            customer.addRole(CustomerRole.USER);
            //데이터베이스에 적용(저장)
            customerRepository.save(customer);
            //MemberSecurityDTO구성 및 반환
            CustomerSecurityDTO customerSecurityDTO=new CustomerSecurityDTO(email, "1111",email,false,true,
                    //SimpleGrantedAuthority: GrantedAuthority를 상속받은 클래스.
                    //GrantedAuthority: 현재 사용자가 가지고있는 권한. ROLE_USER 형태
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            //memberSecurityDTO의 props에 params 값 세팅
            customerSecurityDTO.setProps(params);
            return customerSecurityDTO;
            //result값이 비어있지 않다면(이미 회원이라면)
        }else{
            Customer customer=result.get();
            CustomerSecurityDTO customerSecurityDTO=new CustomerSecurityDTO(
                    customer.getCid(),
                    customer.getCpw(),
                    customer.getEmail(),
                    customer.isDel(),
                    customer.isSocial(),
                    customer.getRoleSet()
                            .stream().map(customerRole -> new SimpleGrantedAuthority("ROLE_"+customerRole.name()))
                            .collect(Collectors.toList())
            );
            return customerSecurityDTO;
        }
    }
    private String getKakaoEmail(Map<String, Object> paramMap){
        log.info("카카오------");
        Object value=paramMap.get("kakao_account");
        log.info(value);
        //LinkedHasMap: Map에 입력된 순서를 기억하는 자료구조임.
        LinkedHashMap accountMap=(LinkedHashMap) value;
        log.info(accountMap);
        String email=(String) accountMap.get("email");
        log.info("이메일 :"+email);
        return email;
    }
}
