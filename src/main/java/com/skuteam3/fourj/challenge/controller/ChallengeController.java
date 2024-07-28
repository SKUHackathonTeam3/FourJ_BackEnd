package com.skuteam3.fourj.challenge.controller;

import com.skuteam3.fourj.challenge.dto.WeeklyChallengeResponseDto;
import com.skuteam3.fourj.challenge.service.ChallengeService;
import com.skuteam3.fourj.global.message.dto.JsonMessageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name="challenges", description="금주, 절주 챌린지 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
@RestController
@Slf4j
public class ChallengeController {

    private final ChallengeService challengeService;

    @Operation(
            summary = "현재 진행중인 금주 챌린지 조회",
            description = "로그인한 유저의 현재 진행중인 금주 챌린지를 조회할 수 있습니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 현재 진행 중인 금주 챌린지를 조회합니다.(종료 되지 않은 금주 챌린지) " +
                    "※금주 챌린지는 종료 API를 사용하여 종료하는 시점에서 성공 실패가 업데이트 됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Abtis returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WeeklyChallengeResponseDto.class)
                    )

            ),
    })
    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklyChallengeInfo(Authentication authentication) {

        WeeklyChallengeResponseDto dto = null;

        try {

            dto = challengeService.getOngoingWeeklyChallenge(authentication.getName());
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to get weekly challenge: " + rse.getMessage()));
        } catch (Exception e) {

            log.error("ChallengeController_getWeeklyChallengeInfo" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonMessageResponseDto("Failed to get weekly challenge"));
        }
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "현재 로그인한 유저의 금주 챌린지 생성",
            description = "로그인한 유저에 금주 챌린지를 생성합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저와 금주 챌린지를 연결하여 생성합니다. " +
                    "금주 챌린지는 생성한 날짜를 기준으로 +14일을 목표 날짜로 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "금주 챌린지 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WeeklyChallengeResponseDto.class)
                    )

            ),
    })
    @PostMapping("/weekly")
    public ResponseEntity<?> createWeeklyChallenge(Authentication authentication) {

        WeeklyChallengeResponseDto dto = null;

        try {

            dto = challengeService.createWeeklyChallenge(authentication.getName());
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to create weekly challenge: " + rse.getMessage()));
        } catch (Exception e) {

            log.error("ChallengeController_createWeeklyChallenge" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonMessageResponseDto("Failed to create weekly challenge"));
        }

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "현재 진행중인 금주 챌린지 종료",
            description = "로그인한 유저의 현재 진행중인 금주 챌린지를 종료할 수 있습니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 현재 진행 중인 금주 챌린지를 조회합니다.(종료 되지 않은 금주 챌린지) " +
                    "API를 사용하는 날짜가 금주 챌린지의 목표날짜 이상인 경우 금주 챌린지를 성공으로 기록합니다. "
    )
    @PostMapping("/weekly/achieved")
    public ResponseEntity<?> updateWeeklyChallengeAchievedSuccess(Authentication authentication) {

        try {

            challengeService.updateWeeklyChallengeAchieved(authentication.getName(), true);
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to update weekly challenge achieved: " + rse.getMessage()));
        } catch (Exception e) {

            log.error("ChallengeController_updateWeeklyChallengeAchievedSuccess" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonMessageResponseDto("Failed to update weekly challenge achieved"));
        }

        return ResponseEntity.ok(new JsonMessageResponseDto("Success to update weekly challenge achieved true"));
    }
}
