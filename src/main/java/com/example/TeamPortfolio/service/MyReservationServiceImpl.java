package com.example.TeamPortfolio.service;

import com.example.TeamPortfolio.domain.Reservation;
import com.example.TeamPortfolio.dto.CustomerJoinDTO;
import com.example.TeamPortfolio.dto.ReservationDTO;
import com.example.TeamPortfolio.repository.CustomerRepository;
import com.example.TeamPortfolio.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyReservationServiceImpl implements MyReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<Reservation> findAllReservations(Principal principal) {
        String cid = principal.getName();
        return reservationRepository.findByUsername(cid);
    }

    @Override
    public ResponseEntity<Void> cancelReservation(Long reservation_id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservation_id);
        if (optionalReservation.isPresent()) {
            reservationRepository.deleteById(reservation_id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public Optional<Reservation> findReservation(Long reservation_id) {
        return reservationRepository.findById(reservation_id);
    }

}

