package com.skuteam3.fourj.attendance.controller;

import com.skuteam3.fourj.attendance.dto.AttendanceListRequestDto;
import com.skuteam3.fourj.attendance.dto.AttendanceRequestDto;
import com.skuteam3.fourj.attendance.service.AttendanceService;
import com.skuteam3.fourj.global.message.dto.JsonMessageResponseDto;
import com.skuteam3.fourj.mission.dto.MissionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name="attendances", description="출석체크 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendance")
@RestController
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(
            summary = "출석 체크",
            description = "로그인한 유저의 출석체크를 합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며, " +
                    "해당 유저의 출석 정보를 저장합니다. "
    )
    @PostMapping
    public ResponseEntity<?> checkIn(Authentication authentication) {

        String userEmail;
        try {

            userEmail = authentication.getName();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JsonMessageResponseDto("토큰 인증에 실패하였습니다."));
        }

        try {

            attendanceService.checkIn(userEmail);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("출석체크에 실패하였습니다 :" +rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonMessageResponseDto("출석체크에 실패하였습니다."));
        }
    }
    @Operation(
            summary = "주간 출석 체크 현황 조회",
            description = "로그인한 유저의 주간 출석체크 현황을 조회합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며, " +
                    "해당 유저의 주간 출석 정보를 불러와 boolean값으로 조회합니다. "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "주간 출석 정보 조회 Response",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AttendanceListRequestDto.class)
                    ))})
    @GetMapping("/week")
    public ResponseEntity<?> getWeeklyAttendance(Authentication authentication) {

        String userEmail;
        try {

            userEmail = authentication.getName();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JsonMessageResponseDto("토큰 인증에 실패하였습니다."));
        }

        try {

            AttendanceListRequestDto attendanceListRequestDto = attendanceService.getWeeklyAttendance(userEmail);
            return ResponseEntity.ok(attendanceListRequestDto);
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("주간 출석일 조회를 실패하였습니다 :" +rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonMessageResponseDto("주간 출석일 조회를 실패하였습니다."));
        }
    }

    @Operation(
            summary = "연속 출석 일수 조회",
            description = "로그인한 유저의 연속 출석 일수를 조회합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며, " +
                    "해당 유저의 연속 출석 일수를 조회합니다. "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "연속 출석 일수 조회 성공 Response",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AttendanceRequestDto.class)
                            ))})
    @GetMapping("/continuous")
    public ResponseEntity<?> getContinuousAttendanceDays(Authentication authentication) {

        String userEmail;
        try {

            userEmail = authentication.getName();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JsonMessageResponseDto("토큰 인증에 실패하였습니다."));
        }

        try {

            AttendanceRequestDto attendanceRequestDto = attendanceService.getContinuousAttendanceDays(userEmail);
            return ResponseEntity.ok(attendanceRequestDto);
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("연속 출석일 조회를 실패하였씁니다 :" +rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonMessageResponseDto("연속 출석일 조회를 실패하였습니다."));
        }
    }
}
