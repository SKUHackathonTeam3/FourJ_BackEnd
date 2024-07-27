package com.skuteam3.fourj.challenge.dto;

import com.skuteam3.fourj.challenge.domain.WeeklyChallenge;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class WeeklyChallengeDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate goalDate;
    private Boolean achieved;

    @Builder
    public WeeklyChallengeDto(Long id, LocalDate startDate, LocalDate endDate, LocalDate goalDate, Boolean achieved) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goalDate = goalDate;
        this.achieved = achieved;
    }

    public static WeeklyChallenge toEntity(WeeklyChallengeDto dto) {

        return WeeklyChallenge.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .goalDate(dto.getGoalDate())
                .achieved(dto.getAchieved())
                .build();
    }

    public static WeeklyChallengeDto of(WeeklyChallenge entity) {
        return WeeklyChallengeDto.builder()
                .id(entity.getId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .goalDate(entity.getGoalDate())
                .achieved(entity.getAchieved())
                .build();
    }
}
