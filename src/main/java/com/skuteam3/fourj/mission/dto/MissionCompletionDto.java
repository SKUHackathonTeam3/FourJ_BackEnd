package com.skuteam3.fourj.mission.dto;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.mission.domain.Mission;
import com.skuteam3.fourj.mission.domain.MissionCompletion;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class MissionCompletionDto {
    private Long id;
    private UserInfo userInfo;
    private Mission mission;

    @Builder
    public MissionCompletionDto(Long id, UserInfo userInfo, Mission mission) {
        this.id = id;
        this.userInfo = userInfo;
        this.mission = mission;
    }

    public static MissionCompletionDto of(MissionCompletion missionCompletion) {
        return MissionCompletionDto.builder()
               .id(missionCompletion.getId())
               .userInfo(missionCompletion.getUserInfo())
               .mission(missionCompletion.getMission())
               .build();
    }

    public static MissionCompletion toEntity(MissionCompletionDto dto) {
        return MissionCompletion.builder()
                .userInfo(dto.userInfo)
                .mission(dto.mission)
                .build();
    }
}
