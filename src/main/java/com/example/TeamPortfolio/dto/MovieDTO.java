package com.example.TeamPortfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private String movieName;
    private String movieDate;
    private String movieAge;
    private Long movieCost;
}
