package com.skuteam3.fourj.mission.service;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.mission.domain.Mission;
import com.skuteam3.fourj.mission.domain.MissionCompletion;
import com.skuteam3.fourj.mission.dto.MissionCompletionDto;
import com.skuteam3.fourj.mission.dto.MissionDto;
import com.skuteam3.fourj.mission.repository.MissionCompletionRepository;
import com.skuteam3.fourj.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MissionService {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final MissionCompletionRepository missionCompletionRepository;

    public List<MissionDto> getAllMissions() {
        return missionRepository.findAll()
                .stream()
                .map(MissionDto::of)
                .collect(Collectors.toList());
    }

    public void clearMission(Long missionId, String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User does not exist")).getUserInfo();

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "mission not found"));

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        boolean alreadyCleared = missionCompletionRepository.existsByMissionAndUserInfoAndClearedAtBetween(
            mission, userInfo, todayStart, todayEnd
        );

        if (!alreadyCleared) {

            MissionCompletion missionCompletion = MissionCompletion.builder()
                    .mission(mission)
                    .userInfo(userInfo)
                    .build();

            missionCompletionRepository.save(missionCompletion);
        } else {

            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mission has already been cleared today");
        }
    }

    public List<MissionCompletionDto> getUserCompletedMissions(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User does not exist")).getUserInfo();

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        return missionCompletionRepository.findByUserInfoAndClearedAtBetween(userInfo, todayStart, todayEnd)
                .stream().map(MissionCompletionDto::of).collect(Collectors.toList());
    }
}
