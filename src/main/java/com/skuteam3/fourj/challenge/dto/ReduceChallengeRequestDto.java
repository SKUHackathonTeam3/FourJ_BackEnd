package com.skuteam3.fourj.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReduceChallengeRequestDto {

    @Schema(description="1주차 절주 목표", example="1.5")
    private double firstWeekDrinkGoal;
    @Schema(description="2주차 절주 목표", example="1.0")
    private double secondWeekDrinkGoal;
}
