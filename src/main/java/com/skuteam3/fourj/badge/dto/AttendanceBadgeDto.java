package com.skuteam3.fourj.badge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttendanceBadgeDto {

    @Schema(description="유저의 최대 연속 출석 횟수", example="30")
    private int maximumAttendanceDays;
}
