package com.skuteam3.fourj.challenge.controller;

import com.skuteam3.fourj.account.domain.CustomUserDetails;
import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.challenge.dto.WeeklyChallengeDto;
import com.skuteam3.fourj.challenge.service.ChallengeService;
import com.skuteam3.fourj.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
@RestController
@Slf4j
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklyChallengeInfo(Authentication authentication) {

        WeeklyChallengeDto dto = null;

        try {

            dto = challengeService.getOngoingWeeklyChallenge(authentication.getName());
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body("Failed to get weekly challenge: " + rse.getMessage());
        } catch (Exception e) {

            log.error("ChallengeController_getWeeklyChallengeInfo" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get weekly challenge");
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/weekly")
    public ResponseEntity<?> createWeeklyChallenge(Authentication authentication) {

        WeeklyChallengeDto dto = null;

        try {

            dto = challengeService.createWeeklyChallenge(authentication.getName());
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body("Failed to create weekly challenge: " + rse.getMessage());
        } catch (Exception e) {

            log.error("ChallengeController_createWeeklyChallenge" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create weekly challenge");
        }

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/weekly/achieved")
    public ResponseEntity<?> updateWeeklyChallengeAchievedSuccess(Authentication authentication) {

        try {

            challengeService.updateWeeklyChallengeAchieved(authentication.getName(), true);
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body("Failed to update weekly challenge achieved: " + rse.getMessage());
        } catch (Exception e) {

            log.error("ChallengeController_updateWeeklyChallengeAchievedSuccess" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update weekly challenge achieved");
        }

        return ResponseEntity.ok("Success to update weekly challenge achieved true");
    }
}
