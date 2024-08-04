package com.skuteam3.fourj.badge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MissionBadgeDto {
    @Schema(description="총 미션 성공 횟수", example="5")
    private int totalMissionCompleted;
}
