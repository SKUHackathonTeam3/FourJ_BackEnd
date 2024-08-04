package com.skuteam3.fourj.badge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeeklyChallengeBadgeDto {

    @Schema(description="금주 챌린지 성공 여부", example="false")
    private boolean existsSuccessWeeklyChallenge;
}
