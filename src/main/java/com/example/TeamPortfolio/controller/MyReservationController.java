package com.example.TeamPortfolio.controller;

import com.example.TeamPortfolio.domain.Reservation;
import com.example.TeamPortfolio.service.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/my")
@Log4j2
public class MyReservationController {

    @Autowired
    private MyReservationService myReservationService;

    @Autowired
    private ReservationService reservationService;

    // 인증된 사용자 = cid > 예매내역만 보여지게끔
    @GetMapping("/Reservation")
    public String findAllReservations(Principal principal, Model model) {
        List<Reservation> reservations = myReservationService.findAllReservations(principal);

        model.addAttribute("reservations", reservations);

        return "my/myReservation";
    }

    // 0807 수정 코드 _ 예약취소
    @DeleteMapping("/Reservation/{reservation_id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservation_id, Principal principal) {
        Optional<Reservation> optionalReservation = myReservationService.findReservation(reservation_id);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            // 현재 로그인한 사용자와 예약의 cid가 일치하는지 확인
            if (reservation.getUsername().equals(principal.getName())) {
                myReservationService.cancelReservation(reservation_id);
                return ResponseEntity.noContent().build();
            } else {
                log.error("User mismatch for reservation id: {}", reservation_id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            log.error("Reservation not found for id: {}", reservation_id);
            return ResponseEntity.notFound().build();
        }
    }
}

