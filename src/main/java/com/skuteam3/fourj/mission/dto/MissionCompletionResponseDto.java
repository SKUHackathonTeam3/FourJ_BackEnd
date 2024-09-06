package com.skuteam3.fourj.mission.dto;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.mission.domain.Mission;
import com.skuteam3.fourj.mission.domain.MissionCompletion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MissionCompletionResponseDto {

    @Schema(description = "클리어 미션 ID", example="1")
    private Long id;
    @Schema(description="미션 ID", example="1")
    private Long missionId;
    @Schema(description="클리어 기록된 시간", example="2024-07-28T15:28:00.129Z")
    private LocalDateTime clearedAt;

    @Builder
    public MissionCompletionResponseDto(Long id, Mission mission, LocalDateTime clearedAt) {
        this.id = id;
        this.missionId = mission.getId();
        this.clearedAt = clearedAt;
    }

    public static MissionCompletionResponseDto of(MissionCompletion missionCompletion) {
        return MissionCompletionResponseDto.builder()
                .id(missionCompletion.getId())
                .mission(missionCompletion.getMission())
                .clearedAt(missionCompletion.getClearedAt())
                .build();
    }

    @Override
    public String toString() {
        return "MissionCompletionDto{" +
                "missionId=" + missionId +
                "clearedAt=" + clearedAt +
                '}';
    }
}
