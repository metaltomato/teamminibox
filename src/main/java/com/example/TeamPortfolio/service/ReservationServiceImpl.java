package com.example.TeamPortfolio.service;

import com.example.TeamPortfolio.domain.Customer;
import com.example.TeamPortfolio.domain.Reservation;
import com.example.TeamPortfolio.dto.ReservationDTO;
import com.example.TeamPortfolio.repository.CustomerRepository;
import com.example.TeamPortfolio.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ModelMapper modelMapper;
    private final ReservationRepository reservationRepository;
    private final DataSource dataSource;
    private final CustomerRepository customerRepository;

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public String getMovieNameFromDatabase() {
        String movie_nm = null;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT movie_nm FROM movie_api")) {
            if (resultSet.next()) {
                movie_nm = resultSet.getString("movie_nm");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie_nm;
    }

    public String getMovieAgeFromDatabase() {
        String movie_age = null;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT movie_age FROM movie")) {
            if (resultSet.next()) {
                movie_age = resultSet.getString("movie_age");
            }
        } catch (SQLException e) {

        }
        return movie_age;
    }

    public String getMovieCostFromDatabase() {
        String movie_cost = null;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT movie_cost FROM movie")) {
            if (resultSet.next()) {
                movie_cost = resultSet.getString("movie_cost");
            }
        } catch (SQLException e) {

        }
        return movie_cost;
    }

    public String getMovieDateFromDatabase() {
        String movie_date = null;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT movie_date FROM movie")) {
            if (resultSet.next()) {
                movie_date = resultSet.getString("movie_date");
            }
        } catch (SQLException e) {

        }
        return movie_date;
    }

    @Override
    public void processReservation(ReservationDTO reservationDTO) {
        log.info("예약 정보: {}", reservationDTO);
    }


    // 예약내역 취소 버튼 클릭 시 해당 예약번호 데이터 삭제
    @Override
    public boolean cancelReservation(Long reservation_Id) {
        try {
            reservationRepository.deleteById(reservation_Id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // 등급 설정
    @Override
    public int countReservationsByCID(String cid) {
        return reservationRepository.countByUsername(cid);
    }
    
    @Override
    public void updateCustomerGrade(String cid) {
        Customer customer = customerRepository.findByCid(cid);
        int reservationCount = countReservationsByCID(cid);

        String grade;
        if (reservationCount >= 5) {
            grade = "Gold";
        } else {
            grade = "Silver";
        }

        customer.setGrade(grade);
        customerRepository.save(customer);
    }

    // cid 사용하여 예약 갯수를 가져오는 메서드
    @Override
    public String getUserGrade(String cid) {
        Customer customer = customerRepository.findByCid(cid);
        int reservationCount = countReservationsByCID(cid);

        String grade;
        if (reservationCount >= 5) {
            grade = "Gold";
        } else {
            grade = "Silver";
        }

        return grade;
    }

    @Override
    public List<ReservationDTO> findAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationDTO::forOccupiedSeat)
                .collect(Collectors.toList());
    }

}