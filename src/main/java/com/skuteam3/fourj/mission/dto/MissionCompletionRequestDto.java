package com.skuteam3.fourj.mission.dto;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.mission.domain.Mission;
import com.skuteam3.fourj.mission.domain.MissionCompletion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class MissionCompletionRequestDto {
    @Schema(description="유저 인포 ID", example="1")
    private UserInfo userInfo;
    @Schema(description="mission", example="1")
    private Mission mission;

    @Builder
    public MissionCompletionRequestDto(UserInfo userInfo, Mission mission) {
        this.userInfo = userInfo;
        this.mission = mission;
    }

    public static MissionCompletionRequestDto of(MissionCompletion missionCompletion) {
        return MissionCompletionRequestDto.builder()
               .userInfo(missionCompletion.getUserInfo())
               .mission(missionCompletion.getMission())
               .build();
    }

    public static MissionCompletion toEntity(MissionCompletionRequestDto dto) {
        return MissionCompletion.builder()
                .userInfo(dto.userInfo)
                .mission(dto.mission)
                .build();
    }

    @Override
    public String toString() {
        return "MissionCompletionDto{" +
                "userInfoId=" + userInfo.getId() +
                ", missionId=" + mission.getId() +
                '}';
    }
}
