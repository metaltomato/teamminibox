package com.example.TeamPortfolio.repsitory;

import com.example.TeamPortfolio.domain.Customer;
import com.example.TeamPortfolio.domain.CustomerRole;
import com.example.TeamPortfolio.repository.CustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.util.Optional;
import java.util.stream.IntStream;


//회원가입 데이터를 추가하기
@SpringBootTest
@Log4j2
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertMembers() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Customer customer = Customer.builder()
                    .cid("customer" + i)
                    .cpw(passwordEncoder.encode("1111"))
                    .email("email" + i + "@gmail.com")
                    .build();
            //모든 멤버에 USER 사용자 권한 추가
            customer.addRole(CustomerRole.USER);
            //i값이 90이상인 멤버에 ADMIN 관리자 권한 추가
            if (i >= 90) {
                customer.addRole(CustomerRole.ADMIN);
            }
            //DB에 데이터 저장
            customerRepository.save(customer);
        });
    }
    //회원 조회 테스트
    @Test
    public void testRead(){
        //데이터베이스에서 아이디가 member100인 데이터의 권한을 찾아서 result에 저장
        Optional<Customer> result=customerRepository.getWithRoles("hj");
        //result의 값에 오류가 있으면 예외처리
        Customer customer=result.orElseThrow();
        //member객체의 정보 표시
        log.info(customer);
        //member100객체의 설정된 권한 표시
        log.info(customer.getRoleSet());
        //권한이 USER,ADMIN 2개이므로 2번 반복
        customer.getRoleSet().forEach(customerRole -> {
            //권한 이름을 콘솔에 표시
            log.info(customerRole.name());
        });
    }
    //비밀번호 수정하기 테스트
    @Commit
    @Test
    public void testUpdate(){
        String cid="user2";
        String cpw= passwordEncoder.encode("2222");
        customerRepository.updatePassword(cpw, cid);
    }




}