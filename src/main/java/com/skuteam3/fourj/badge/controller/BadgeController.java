package com.skuteam3.fourj.badge.controller;

import com.skuteam3.fourj.badge.service.BadgeService;
import com.skuteam3.fourj.global.message.dto.JsonMessageResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "badges", description="뱃지 정보 API")
@RequestMapping("/api/v1/badges")
@RequiredArgsConstructor
@RestController
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping("/attendance")
    public ResponseEntity<?> getUserAttendanceBadge(Authentication authentication) {

        if (authentication == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(badgeService.getUserAttendanceBadge(authentication.getName()));
    }

    @GetMapping("/mission")
    public ResponseEntity<?> getMissionBadge(Authentication authentication) {

        if (authentication == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(badgeService.getMissionBadge(authentication.getName()));
    }


    @GetMapping("/weekly-challenge")
    public ResponseEntity<?> getWeeklyChallengeBadge(Authentication authentication) {

        if (authentication == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(badgeService.getWeeklyChallengeBadge(authentication.getName()));
    }
}
