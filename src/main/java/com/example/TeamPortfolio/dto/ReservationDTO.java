package com.example.TeamPortfolio.dto;

import com.example.TeamPortfolio.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private Long reservation_id; // SQL 에서는 reservation_id 이 컬럼명임
    private String reservation_day;
    private String reservation_num;
    
    private String cid;
    private Long rank;
    private String movieNm;
    private String image;

    // reservation 테이블 전체 데이터 마이페이지,마이예약페이지로 가져가서 사용합니당
    public static ReservationDTO fromReservation(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setReservation_id(reservation.getReservation_id());
        reservationDTO.setReservation_day(reservation.getReservation_day());
        reservationDTO.setReservation_num(reservation.getReservation_num());
        reservationDTO.setImage(reservation.getImage());
        reservationDTO.setMovieNm(reservation.getMovieNm());
        return reservationDTO;
    }

    // start.html 페이지에 reservation_num 가져가기
    public static ReservationDTO forOccupiedSeat(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setReservation_num(reservation.getReservation_num());
        return reservationDTO;
    }
}