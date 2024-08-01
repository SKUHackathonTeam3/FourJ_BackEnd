package com.skuteam3.fourj.mission.dto;

import com.skuteam3.fourj.mission.domain.Mission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class MissionResponseDto {

    @Schema(description="미션 Id", example="1")
    private Long id;
    @Schema(description="미션 내용", example="출석체크 하기")
    private String missionText;

    @Builder
    public MissionResponseDto(Long id, String missionText) {
        this.id = id;
        this.missionText = missionText;
    }

    public static MissionResponseDto of(Mission mission) {
        return MissionResponseDto.builder()
                .id(mission.getId())
                .missionText(mission.getMissionText())
                .build();
    }

    public static Mission toEntity(MissionResponseDto missionResponseDto) {
        return Mission.builder()
                .missionText(missionResponseDto.getMissionText())
                .build();
    }
}
