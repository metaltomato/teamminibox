package com.example.TeamPortfolio.repository;

import com.example.TeamPortfolio.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 회원 등급 속성 추가 시 필요한 메서드
    int countByUsername(String username);

    @Query(value = "SELECT reservation_num FROM reservation WHERE movie_nm = :movie_api", nativeQuery = true)
    List<String> findReservedSeats(@Param("movie_api") String movie);

    // 인증된 사용자와 cid가 같은 예약내역만 불러오는 메서드
    List<Reservation> findByUsername(String username);
}
