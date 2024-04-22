package com.example.TeamPortfolio.domain;

import jakarta.persistence.*;
import lombok.*;

import javax.swing.*;
import java.time.LocalDateTime;

@Entity(name = "reservation")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservation_id;

    @Column(length = 20, nullable = false)
    private String reservation_day;

    @Column(length = 1000, nullable = false)
    private String reservation_num;

    @Column(name = "cid")
    private String username;

    @Column(name= "ranking")
    private Long rank;
    private String movieNm;
    private String image;

}