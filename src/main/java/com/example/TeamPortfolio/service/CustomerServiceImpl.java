package com.example.TeamPortfolio.service;

import com.example.TeamPortfolio.domain.Customer;
import com.example.TeamPortfolio.domain.CustomerRole;
import com.example.TeamPortfolio.domain.Reservation;
import com.example.TeamPortfolio.dto.CustomerJoinDTO;
import com.example.TeamPortfolio.repository.CustomerRepository;
import com.example.TeamPortfolio.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(CustomerJoinDTO customerJoinDTO) throws cidExistException{
        String cid=customerJoinDTO.getCid();
        boolean exist=customerRepository.existsById(cid);
        if(exist){
            throw new cidExistException();
        }
        Customer customer=modelMapper.map(customerJoinDTO, Customer.class);
        customer.changePassword(passwordEncoder.encode(customerJoinDTO.getCpw()));
        customer.addRole(CustomerRole.USER);
        log.info("**********************");
        log.info(customer);
        log.info(customer.getRoleSet());
        customerRepository.save(customer);
    }

    //로그인한 사용자 이름(이메일) 가져오기ㅣ
    @Override
    public UserDetails loadUserByUsername(String cid) throws UsernameNotFoundException{
        Optional<Customer> _memUser=this.customerRepository.findById(cid);
        if(_memUser.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        Customer customer = _memUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if("admin".equals(cid)) {
            authorities.add(new SimpleGrantedAuthority(CustomerRole.ADMIN.getValue()));

        }else{
            authorities.add(new SimpleGrantedAuthority(CustomerRole.USER.getValue()));
        }
        return new User(customer.getCid(), customer.getCpw(), authorities);
    }

}


