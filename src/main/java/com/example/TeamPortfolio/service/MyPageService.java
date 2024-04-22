package com.example.TeamPortfolio.service;

import com.example.TeamPortfolio.domain.Customer;
import com.example.TeamPortfolio.domain.Reservation;
import com.example.TeamPortfolio.dto.CustomerJoinDTO;
import com.example.TeamPortfolio.dto.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface MyPageService {

    List<ReservationDTO> findAllReservations();
    List<CustomerJoinDTO> findAllCustomers();

}
