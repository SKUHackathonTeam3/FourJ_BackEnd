package com.skuteam3.fourj.challenge.service;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.challenge.domain.WeeklyChallenge;
import com.skuteam3.fourj.challenge.dto.WeeklyChallengeResponseDto;
import com.skuteam3.fourj.challenge.repository.WeeklyChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

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

        try {

            if (getOngoingWeeklyChallenge(userEmail) != null) {

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already has an ongoing weekly challenge");
            }
        } catch (ResponseStatusException ignored) {
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

    public void updateWeeklyChallengeAchieved(String userEmail, boolean achieved) {

        WeeklyChallenge weeklyChallenge = weeklyChallengeRepository.findById(
                getOngoingWeeklyChallenge(userEmail).getId()
        ).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have an ongoing weekly challenge")
        );

        if (achieved) {

            LocalDate today = LocalDate.now();
            if (weeklyChallenge.getGoalDate().isAfter(today)) {

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Goal date must be before today");
            }
        }

        weeklyChallenge.setAchieved(achieved);
        weeklyChallengeRepository.save(weeklyChallenge);
    }

    public boolean findSuccessfulWeeklyChallenge(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        ).getUserInfo();

        return weeklyChallengeRepository.existsByAchievedIsTrueAndUserInfo(userInfo);
    }
}
