package com.skuteam3.fourj.challenge.service;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.challenge.domain.WeeklyChallenge;
import com.skuteam3.fourj.challenge.dto.WeeklyChallengeResponseDto;
import com.skuteam3.fourj.challenge.repository.WeeklyChallengeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Transactional
@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final WeeklyChallengeRepository weeklyChallengeRepository;
    private final UserRepository userRepository;

    public WeeklyChallengeResponseDto createWeeklyChallenge(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        ).getUserInfo();
        LocalDate now = LocalDate.now();


        if (weeklyChallengeRepository.findByAchievedIsNullAndUserInfo(userInfo).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already has an ongoing weekly challenge");
        }

        WeeklyChallenge weeklyChallenge = WeeklyChallenge.builder()
                .startDate(now)
                .goalDate(now.plusWeeks(2))
                .build();
        weeklyChallenge.setUserInfo(userInfo);

        return WeeklyChallengeResponseDto.of(weeklyChallengeRepository.save(weeklyChallenge));
    }

    public WeeklyChallengeResponseDto getOngoingWeeklyChallenge(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        ).getUserInfo();

        WeeklyChallenge weeklyChallenge = weeklyChallengeRepository.findByAchievedIsNullAndUserInfo(userInfo)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have an ongoing weekly challenge")
        );

        return WeeklyChallengeResponseDto.of(weeklyChallenge);
    }

    public void updateWeeklyChallengeEndDate(String userEmail, LocalDate endDate) {

        WeeklyChallenge weeklyChallenge = weeklyChallengeRepository.findById(
                getOngoingWeeklyChallenge(userEmail).getId()
        ).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have an ongoing weekly challenge")
        );

        weeklyChallenge.setEndDate(endDate);

        if (weeklyChallenge.getGoalDate().isAfter(endDate)) {

            weeklyChallenge.setAchieved(false);
        }

        weeklyChallengeRepository.save(weeklyChallenge);
    }

    public boolean updateWeeklyChallengeAchieved(String userEmail) {

        Boolean achieved = null;

        WeeklyChallenge weeklyChallenge = weeklyChallengeRepository.findById(
                getOngoingWeeklyChallenge(userEmail).getId()
        ).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have an ongoing weekly challenge")
        );

        LocalDate today = LocalDate.now();
        if (weeklyChallenge.getGoalDate().isAfter(today)) {

            weeklyChallenge.setAchieved(false);
            achieved = false;
        } else {

            weeklyChallenge.setAchieved(true);
            achieved = true;
        }

        weeklyChallengeRepository.save(weeklyChallenge);
        return achieved;
    }

    public boolean findSuccessfulWeeklyChallenge(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        ).getUserInfo();

        return weeklyChallengeRepository.existsByAchievedIsTrueAndUserInfo(userInfo);
    }
}
