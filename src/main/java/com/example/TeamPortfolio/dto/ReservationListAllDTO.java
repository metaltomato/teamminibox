package com.example.TeamPortfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationListAllDTO {
    private Long reservation_id;
    private String zone;
    private String location;
    private int cost;
}
