package com.example.TeamPortfolio.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class getDateDTO {
    @Min(value = 1, message = "월이 1월보다 작습니다.")
    @Max(value = 12, message = "월이 12월보다 큽니다.")
    private int month;

    @Min(value = 2023, message = "연도가 2023보다 작습니다.")
    private int year;

    @NotBlank(message = "자리가 빈칸입니다.")
    private String seat;
}
