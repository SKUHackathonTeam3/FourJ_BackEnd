package com.skuteam3.fourj.mission.repository;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.mission.domain.Mission;
import com.skuteam3.fourj.mission.domain.MissionCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MissionCompletionRepository extends JpaRepository<MissionCompletion, Long> {

    boolean existsByMissionAndUserInfoAndClearedAtBetween(Mission mission, UserInfo userInfo, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<MissionCompletion> findByUserInfoAndClearedAtBetween(UserInfo userInfo, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<MissionCompletion> findByUserInfo(UserInfo userInfo);
}