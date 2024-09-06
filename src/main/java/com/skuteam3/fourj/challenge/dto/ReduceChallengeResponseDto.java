package com.skuteam3.fourj.challenge.dto;

import com.skuteam3.fourj.challenge.domain.WeeklyChallenge;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class ReduceChallengeResponseDto extends WeeklyChallengeResponseDto{
    @Schema(description="1주차 절주 목표", example="1.5")
    private Double firstWeekDrinkGoal;
    @Schema(description="2주차 절주 목표", example="1.0")
    private Double secondWeekDrinkGoal;

    public ReduceChallengeResponseDto(
            Long id,
            LocalDate startDate,
            LocalDate endDate,
            LocalDate goalDate,
            Double firstWeekDrinkGoal,
            Double secondWeekDrinkGoal,
            Boolean achieved,
            boolean isReduce,
            int remainingDate) {

        super(id, startDate, endDate, goalDate, achieved, remainingDate, isReduce);

        this.firstWeekDrinkGoal = firstWeekDrinkGoal;
        this.secondWeekDrinkGoal = secondWeekDrinkGoal;
    }

    public static WeeklyChallenge toEntity(ReduceChallengeResponseDto dto) {

        return WeeklyChallenge.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .goalDate(dto.getGoalDate())
                .firstWeekDrinkGoal(dto.getFirstWeekDrinkGoal())
                .secondWeekDrinkGoal(dto.getSecondWeekDrinkGoal())
                .isReduce(true)
                .achieved(dto.getAchieved())
                .build();
    }

    public static ReduceChallengeResponseDto of(WeeklyChallenge entity) {

        return new ReduceChallengeResponseDto(
                entity.getId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getGoalDate(),
                entity.getFirstWeekDrinkGoal(),
                entity.getSecondWeekDrinkGoal(),
                entity.getAchieved(),
                entity.getIsReduce(),
                calculateBetweenDate(entity.getStartDate(), entity.getGoalDate())
        );
    }

    public static int calculateBetweenDate(LocalDate startDate, LocalDate endDate) {
        int between = (int) ChronoUnit.DAYS.between(startDate, endDate);
        if (between <= 0) between = 0;

        return between;
    }
}
