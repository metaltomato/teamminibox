package com.example.TeamPortfolio.service;

import com.example.TeamPortfolio.domain.Customer;
import com.example.TeamPortfolio.domain.Reservation;
//import com.example.TeamPortfolio.repository.MyPageRepository;


import com.example.TeamPortfolio.dto.CustomerJoinDTO;
import com.example.TeamPortfolio.dto.ReservationDTO;
import com.example.TeamPortfolio.repository.CustomerRepository;
import com.example.TeamPortfolio.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyPageServiceImpl implements MyPageService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<ReservationDTO> findAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationDTO::fromReservation)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerJoinDTO> findAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerJoinDTO::fromCustomer)
                .collect(Collectors.toList());
    }
}

