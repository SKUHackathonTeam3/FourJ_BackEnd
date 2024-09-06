package com.skuteam3.fourj.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UpdateDrinkAmountDto {

    @Schema(description="주간 음주량", example="4.0")
    private Double weeklyAlcoholAmount;
    @Schema(description="평균 주량", example="0.5")
    private Double averageAlcoholAmount;
}
