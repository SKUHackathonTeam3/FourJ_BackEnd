package com.skuteam3.fourj.calendar.controller;

import com.skuteam3.fourj.calendar.domain.Schedule;
import com.skuteam3.fourj.calendar.dto.ScheduleRequestDto;
import com.skuteam3.fourj.calendar.dto.ScheduleResponseDto;
import com.skuteam3.fourj.calendar.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "schedules", description = "약속 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    //Create
    @Operation(
            summary = "약속 생성",
            description = "캘린더 ID에 따른 약속을 생성 합니다. " +
                    "해당 캘린더와 schedule을 연결하여 저장합니다.",
            parameters = {
                    @Parameter(name = "calendarId", description = "[path_variable] calendar의 Id", required = true)
            }
    )
    @PostMapping("/{calendarId}")
    public String createSchedule(@PathVariable Long calendarId, @RequestBody ScheduleRequestDto scheduleRequestDto){

        scheduleService.createSchedule(scheduleRequestDto, calendarId);
        return "Schedule created successfully";
    }

    //Read
    @Operation(
            summary = "년, 월 약속 조회",
            description = "캘린더의 년,월 따른 약속을 조회합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포에 저장된 schedule을 년, 월에 따라 모두 조회합니다.",
            parameters = {
                    @Parameter(name = "year", description = "[path_variable] year", required = true),
                    @Parameter(name = "month", description = "[path_variable] month", required = true)
            }
    )
    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<ScheduleResponseDto>> getSchedulesByYearAndMonthAndUserInfo(Authentication authentication, @PathVariable int year, @PathVariable int month) {
        String userEmail = authentication.getName();

        List<Schedule> schedules = scheduleService.getSchedulesByYearAndMonthAndUserInfo(year, month, userEmail);
        List<ScheduleResponseDto> scheduleResponseDtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleResponseDto dto = ScheduleResponseDto.from(schedule);
            scheduleResponseDtos.add(dto);
        }
        return ResponseEntity.ok(scheduleResponseDtos);
    }

    @Operation(
            summary = "년, 월, 일 약속 조회",
            description = "캘린더의 년,월,일 따른 약속을 조회합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포에 저장된 schedule을 년, 월, 일에 따라 모두 조회합니다.",
            parameters = {
                    @Parameter(name = "year", description = "[path_variable] year", required = true),
                    @Parameter(name = "month", description = "[path_variable] month", required = true),
                    @Parameter(name = "day", description = "[path_variable] day", required = true)
            }
    )
    @GetMapping("/{year}/{month}/{day}")
    public ResponseEntity<List<ScheduleResponseDto>> getSchedulesByYearAndMonthAndDayAndUserInfo(Authentication authentication, @PathVariable int year, @PathVariable int month, @PathVariable int day){
        String userEmail = authentication.getName();

        List<Schedule> schedules = scheduleService.getSchedulesByYearAndMonthAndDayAndUserInfo(year, month, day, userEmail);
        List<ScheduleResponseDto> scheduleResponseDtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleResponseDto dto = ScheduleResponseDto.from(schedule);
            scheduleResponseDtos.add(dto);
        }
        return ResponseEntity.ok(scheduleResponseDtos);
    }

    //Update
    @Operation(
            summary = "약속 수정",
            description = "유저인포에 따른 약속을 수정합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 schedule이 맞는 경우 약속을 수정합니다.",
            parameters = {
                    @Parameter(name = "scheduleId", description = "[path_variable] schedule의 Id", required = true)
            }
    )
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        try {
            Schedule updateSchedule = scheduleService.updateSchedule(scheduleId, scheduleRequestDto);
            return ResponseEntity.ok(ScheduleResponseDto.from(updateSchedule));
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @Operation(
            summary = "약속 삭제",
            description = "유저인포에 따른 약속을 삭제합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 schedule이 맞는 경우 약속을 삭제합니다.",
            parameters = {
                    @Parameter(name = "scheduleId", description = "[path_variable] schedule의 Id", required = true)
            }
    )
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId){
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "주간 음주량 계산",
            description = "캘린더의 년,월,일 따른 약속을 조회합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "한 주의 시작 날짜와 마지막 날짜에 따른 음주량을 계산하여 반환합니다.",
            parameters = {
                    @Parameter(name = "year", description = "[path_variable] year", required = true),
                    @Parameter(name = "month", description = "[path_variable] month", required = true),
                    @Parameter(name = "startDay", description = "[path_variable] startDay", required = true),
                    @Parameter(name = "endDay", description = "[path_variable] endDay", required = true)
            }
    )
    @GetMapping("/{year}/{month}/{startDay}/{endDay}")
    public ResponseEntity<Double> calculateWeeklyAlcohol(Authentication authentication, @PathVariable int year, @PathVariable int month, @PathVariable int startDay, @PathVariable int endDay){
        String userEmail = authentication.getName();

        Double weeklyAlcohol = scheduleService.calculateWeeklyAlcohol(year, month, startDay, endDay, userEmail);
        return ResponseEntity.ok(weeklyAlcohol);
    }


}
