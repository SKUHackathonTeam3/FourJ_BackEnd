package com.skuteam3.fourj.badge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserInfoRepository;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.badge.dto.AttendanceBadgeDto;
import com.skuteam3.fourj.badge.dto.MissionBadgeDto;
import com.skuteam3.fourj.badge.dto.WeeklyChallengeBadgeDto;
import com.skuteam3.fourj.challenge.service.ChallengeService;
import com.skuteam3.fourj.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class BadgeService {

    private final UserRepository userRepository;
    private final MissionService missionService;
    private final ChallengeService challengeService;

    public AttendanceBadgeDto getUserAttendanceBadge(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found.")).getUserInfo();

        return new AttendanceBadgeDto(userInfo.getMaximumAttendanceDays());
    }

    public MissionBadgeDto getMissionBadge(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found.")).getUserInfo();

        return new MissionBadgeDto(missionService.getAllCompletedMissions(userEmail).size());
    }

    public WeeklyChallengeBadgeDto getWeeklyChallengeBadge(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found.")).getUserInfo();

        return new WeeklyChallengeBadgeDto(challengeService.findSuccessfulWeeklyChallenge(userEmail, false));
    }

}
