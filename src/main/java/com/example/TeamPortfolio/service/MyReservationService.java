package com.example.TeamPortfolio.service;

import com.example.TeamPortfolio.domain.Reservation;
import com.example.TeamPortfolio.dto.ReservationDTO;
import com.example.TeamPortfolio.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface MyReservationService {

    List<Reservation> findAllReservations(Principal principal);
    ResponseEntity<Void> cancelReservation(Long reservation_id);
    Optional<Reservation> findReservation(Long reservation_id);

}
