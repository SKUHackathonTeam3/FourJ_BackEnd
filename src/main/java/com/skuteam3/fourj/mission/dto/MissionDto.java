package com.skuteam3.fourj.mission.dto;

import com.skuteam3.fourj.mission.domain.Mission;
import lombok.Builder;
import lombok.Data;

@Data
public class MissionDto {

    private Long id;
    private String missionText;

    @Builder
    public MissionDto(Long id, String missionText) {
        this.id = id;
        this.missionText = missionText;
    }

    public static MissionDto of(Mission mission) {
        return MissionDto.builder()
                .id(mission.getId())
                .missionText(mission.getMissionText())
                .build();
    }

    public static Mission toEntity(MissionDto missionDto) {
        return Mission.builder()
                .missionText(missionDto.getMissionText())
                .build();
    }
}
