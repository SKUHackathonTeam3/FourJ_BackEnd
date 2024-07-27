package com.skuteam3.fourj.challenge.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReduceChallengeDto {
    private Long id;
    private LocalDate weekStart;
    private Double drinkGoal;
    private Double weeklyAverageDrink;

    @Builder
    public ReduceChallengeDto(LocalDate weekStart, Double drinkGoal, Double weeklyAverageDrink) {
        this.weekStart = weekStart;
        this.drinkGoal = drinkGoal;
        this.weeklyAverageDrink = weeklyAverageDrink;
    }
}
