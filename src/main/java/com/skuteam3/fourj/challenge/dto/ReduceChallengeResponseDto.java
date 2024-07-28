package com.skuteam3.fourj.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReduceChallengeResponseDto {
    @Schema(description="절주 챌린지 Id", example="1")
    private Long id;
    @Schema(description="시작 주 월요일 날짜", example="2024-01-01")
    private LocalDate weekStart;
    @Schema(description="절주 목표 음주량", example="3.0")
    private Double drinkGoal;
    @Schema(description="이전 평균 음주량", example="3.5")
    private Double weeklyAverageDrink;

    @Builder
    public ReduceChallengeResponseDto(LocalDate weekStart, Double drinkGoal, Double weeklyAverageDrink) {
        this.weekStart = weekStart;
        this.drinkGoal = drinkGoal;
        this.weeklyAverageDrink = weeklyAverageDrink;
    }
}
