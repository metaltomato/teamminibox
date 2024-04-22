package com.example.TeamPortfolio.service;

import com.example.TeamPortfolio.domain.Reservation;
import com.example.TeamPortfolio.dto.ReservationDTO;
import com.example.TeamPortfolio.dto.getDateDTO;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;


public interface ReservationService {
    default Reservation dtoToEntity(ReservationDTO reservationDTO){
        Reservation reservation = Reservation.builder()
                .reservation_id(reservationDTO.getReservation_id())
                .reservation_day(reservationDTO.getReservation_day())
                .reservation_num(reservationDTO.getReservation_num())
                .build();
        return reservation;
    }

    default ReservationDTO entityToDto(Reservation reservation){
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .reservation_id(reservation.getReservation_id())
                .reservation_day(reservation.getReservation_day())
                .reservation_num(reservation.getReservation_num())
                .build();
        return reservationDTO;
    }

    String getMovieNameFromDatabase();
    String getMovieAgeFromDatabase();
    String getMovieCostFromDatabase();
    String getMovieDateFromDatabase();

    void processReservation(ReservationDTO reservationDTO);

    Reservation save(Reservation reservation);

    // 예약취소 버튼 누를 시 해당 예약번호 삭제 기능 구현 _myReservation에서 사용합니당
    boolean cancelReservation(Long reservation_id);

    // 예약 등급 속성 추가
    String getUserGrade(String cid);
    void updateCustomerGrade(String cid);
    int countReservationsByCID(String cid);


    List<ReservationDTO> findAllReservations();

}