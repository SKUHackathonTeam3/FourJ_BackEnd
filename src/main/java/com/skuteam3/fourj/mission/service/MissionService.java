package com.skuteam3.fourj.mission.service;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.mission.domain.Mission;
import com.skuteam3.fourj.mission.domain.MissionCompletion;
import com.skuteam3.fourj.mission.dto.MissionCompletionRequestDto;
import com.skuteam3.fourj.mission.dto.MissionCompletionResponseDto;
import com.skuteam3.fourj.mission.dto.MissionResponseDto;
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

    // mission 목록 반환
    public List<MissionResponseDto> getAllMissions() {
        return missionRepository.findAll()
                .stream()
                .map(MissionResponseDto::of)
                .collect(Collectors.toList());
    }

    // userEmail에 해당하는 유저가 missionId에 해당하는 미션을 클리어한 기록 생성
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

    // userEmail에 해당하는 유저가 오늘 클리어한 미션 목록 반환
    public List<MissionCompletionResponseDto> getUserCompletedMissions(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User does not exist")).getUserInfo();

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        return missionCompletionRepository.findByUserInfoAndClearedAtBetween(userInfo, todayStart, todayEnd)
                .stream().map(MissionCompletionResponseDto::of).collect(Collectors.toList());
    }

    // userEmail에 해당하는 유저의 모든 클리어 미션 목록 반환
    public List<MissionCompletionResponseDto> getAllCompletedMissions(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User does not exist")).getUserInfo();

        return missionCompletionRepository.findByUserInfo(userInfo)
                .stream().map(MissionCompletionResponseDto::of).collect(Collectors.toList());
    }
}
