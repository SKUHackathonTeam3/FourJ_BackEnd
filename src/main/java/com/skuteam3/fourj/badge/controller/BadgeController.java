package com.skuteam3.fourj.badge.controller;

import com.skuteam3.fourj.abti.dto.AbtiResponseDto;
import com.skuteam3.fourj.badge.dto.AttendanceBadgeDto;
import com.skuteam3.fourj.badge.dto.MissionBadgeDto;
import com.skuteam3.fourj.badge.dto.WeeklyChallengeBadgeDto;
import com.skuteam3.fourj.badge.service.BadgeService;
import com.skuteam3.fourj.global.message.dto.JsonMessageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "최대 연속 출석 일수 조회",
            description = "로그인한 유저의 최대 연속 출석 일수를 조회합니다." +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며" +
                    "해당 유저의 최대 연속 출석 일수를 int 값으로 response받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "최대 연속 출석 일수 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AttendanceBadgeDto.class)
                    )),})
    @GetMapping("/attendance")
    public ResponseEntity<?> getUserAttendanceBadge(Authentication authentication) {

        if (authentication == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(badgeService.getUserAttendanceBadge(authentication.getName()));
    }

    @Operation(
            summary = "미션 성공 횟수 조회",
            description = "로그인한 유저의 미션 성공 횟수를 조회합니다." +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며" +
                    "해당 유저의 미션 성공 횟수를 int 값으로 response받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "미션 성공 횟수 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MissionBadgeDto.class)
                    )),})
    @GetMapping("/mission")
    public ResponseEntity<?> getMissionBadge(Authentication authentication) {

        if (authentication == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(badgeService.getMissionBadge(authentication.getName()));
    }


    @Operation(
            summary = "금주 챌린지 성공 여부 확인",
            description = "로그인한 유저의 금주 챌린지 성공 여부를 조회합니다." +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며" +
                    "해당 유저의 금주 챌린지 성공 여부를 boolean 값으로 response받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "금주 챌린지 성공 여부 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WeeklyChallengeBadgeDto.class)
                    )),})
    @GetMapping("/weekly-challenge")
    public ResponseEntity<?> getWeeklyChallengeBadge(Authentication authentication) {

        if (authentication == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(badgeService.getWeeklyChallengeBadge(authentication.getName()));
    }
}
