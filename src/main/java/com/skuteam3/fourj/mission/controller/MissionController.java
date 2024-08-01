package com.skuteam3.fourj.mission.controller;

import com.skuteam3.fourj.abti.dto.AbtiResponseDto;
import com.skuteam3.fourj.global.message.dto.JsonMessageResponseDto;
import com.skuteam3.fourj.mission.dto.MissionCompletionRequestDto;
import com.skuteam3.fourj.mission.dto.MissionCompletionResponseDto;
import com.skuteam3.fourj.mission.dto.MissionResponseDto;
import com.skuteam3.fourj.mission.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Tag(name="missions", description="미션 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/missions")
@RestController
public class MissionController {

    private final MissionService missionService;

    // Mission에 대한 메서드


    // 모든 미션 조회
    @Operation(
            summary = "모든 미션 조회",
            description = "데이터베이스에 저장된 모든 미션을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Missions returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = MissionResponseDto.class)
                            )))})
    @GetMapping
    public ResponseEntity<?> getAllMissions() {

        try {

            return ResponseEntity.ok(missionService.getAllMissions());
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to get missions: " + rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonMessageResponseDto("Failed to get missions"));
        }
    }


    // MissionCompletion에 대한 메서드


    // 미션 클리어 기록 생성
    @Operation(
            summary = "미션 클리어 기록 생성",
            description = "로그인한 유저가 미션을 클리어한 기록을 생성합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며, " +
                    "해당 유저가 미션을 클리어한 기록을 현재 시간과 함께 저장합니다. ",
            parameters = {
                    @Parameter(name = "missionId", description = "[path_variable] 클리어로 기록할 mission의 ID", required = true)
            }
    )
    @PostMapping("/{missionId}/clear")
    public ResponseEntity<?> clearMissions(Authentication authentication, @PathVariable Long missionId) {

        String userEmail;
        try {

            userEmail = authentication.getName();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {

            missionService.clearMission(missionId, userEmail);
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to clear mission: " + rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonMessageResponseDto("Failed to clear mission"));
        }

        return ResponseEntity.ok("Mission cleared");
    }

    // 해당 유저가 오늘 클리어한 미션 목록 조회
    @Operation(
            summary = "오늘 클리어한 미션 목록 조회",
            description = "로그인한 유저의 오늘 클리어한 미션 목록을 조회합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며, " +
                    "해당 유저의 오늘 0시에서 24시 사이에 클리어한 미션의 목록을 조회합니다. "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Completed Missions returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = MissionCompletionResponseDto.class)
                            )))})
    @GetMapping("/completed")
    public ResponseEntity<?> getCompletedMissions(Authentication authentication) {

        String userEmail;
        try {

            userEmail = authentication.getName();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {

            return ResponseEntity.ok(missionService.getUserCompletedMissions(userEmail));
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to get completed missions: " + rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonMessageResponseDto("Failed to get completed missions"));
        }
    }




}
