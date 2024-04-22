package com.example.TeamPortfolio.service;


import com.example.TeamPortfolio.domain.Customer;
import com.example.TeamPortfolio.domain.Reservation;
import com.example.TeamPortfolio.dto.CustomerJoinDTO;
import com.example.TeamPortfolio.repository.CustomerRepository;
import com.example.TeamPortfolio.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


public interface CustomerService {

    //cidExistException:회원이 이미 존재하는 경우 예외처리함.
    static class cidExistException extends Exception{
    }
    void join(CustomerJoinDTO customerJoinDTO) throws cidExistException;

    UserDetails loadUserByUsername(String cid);
}