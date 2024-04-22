package com.example.TeamPortfolio.dto;

import com.example.TeamPortfolio.domain.Customer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Data;

@Data
public class CustomerJoinDTO {
    private String cid;
    private String cpw;
    private String email;
    private String name;
    private String birth;
    private String phone;
    private String grade;
    private boolean del;
    private boolean social;

    // customer 테이블에서 필요한 데이터 마이페이지로 가져갈게용
    public static CustomerJoinDTO fromCustomer(Customer customer) {
        CustomerJoinDTO customerJoinDTO = new CustomerJoinDTO();
        customerJoinDTO.setCid(customerJoinDTO.getCid());
        customerJoinDTO.setGrade(customerJoinDTO.getGrade());
        customerJoinDTO.setName(customerJoinDTO.getName());
        return customerJoinDTO;
    }
}

