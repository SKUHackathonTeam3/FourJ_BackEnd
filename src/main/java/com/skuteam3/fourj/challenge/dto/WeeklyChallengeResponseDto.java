package com.skuteam3.fourj.challenge.dto;

import com.skuteam3.fourj.challenge.domain.WeeklyChallenge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WeeklyChallengeResponseDto {
    @Schema(description="금주 챌린지 Id", example="1")
    private Long id;
    @Schema(description="시작일", example="2024-01-01")
    private LocalDate startDate;
    @Schema(description="챌린지 종료일", example="2024-01-15")
    private LocalDate endDate;
    @Schema(description="목표일", example="2024-01-14")
    private LocalDate goalDate;
    @Schema(description="달성 여부", example="true")
    private Boolean achieved;

    @Builder
    public WeeklyChallengeResponseDto(Long id, LocalDate startDate, LocalDate endDate, LocalDate goalDate, Boolean achieved) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goalDate = goalDate;
        this.achieved = achieved;
    }

    public static WeeklyChallenge toEntity(WeeklyChallengeResponseDto dto) {

        return WeeklyChallenge.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .goalDate(dto.getGoalDate())
                .achieved(dto.getAchieved())
                .build();
    }

    public static WeeklyChallengeResponseDto of(WeeklyChallenge entity) {
        return WeeklyChallengeResponseDto.builder()
                .id(entity.getId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .goalDate(entity.getGoalDate())
                .achieved(entity.getAchieved())
                .build();
    }
}
