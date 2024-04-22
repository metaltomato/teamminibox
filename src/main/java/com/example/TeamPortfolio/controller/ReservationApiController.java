package com.example.TeamPortfolio.controller;

import com.example.TeamPortfolio.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReservationApiController {

    @Autowired
    private ReservationRepository reservationRepository;

    @RequestMapping("/reservedSeats")
    public List<String> getReservedSeats(@RequestParam String movieNm) {
        return reservationRepository.findReservedSeats(movieNm);
    }
}
