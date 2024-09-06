package com.skuteam3.fourj.calendar.controller;

import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.dto.CalendarRequestDto;
import com.skuteam3.fourj.calendar.dto.CalendarResponseDto;
import com.skuteam3.fourj.calendar.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name = "calendars", description = "캘린더 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/calendars")
public class CalendarController {

    private final CalendarService calendarService;

    //Create
    @Operation(
            summary = "캘린더 생성",
            description = "유저인포에 따른 캘린더를 생성 합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포와 calendar를 연결하여 저장합니다."
    )
    @PostMapping
    public String createCalendar(Authentication authentication, @RequestBody CalendarRequestDto calendarRequestDto){
        String userEmail = authentication.getName();

        calendarService.createCalendar(calendarRequestDto, userEmail);
        return "Calendar created successfully";
    }

    //Read
    @Operation(
            summary = "캘린더 조회",
            description = "유저인포에 따른 캘린더를 조회합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포에 저장된 calendar를 모두 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<CalendarResponseDto>> getCalendarByUserEmail(Authentication authentication){
        String userEmail = authentication.getName();

        List<Calendar> calendarList = calendarService.getCalendarByUserEmail(userEmail);
        List<CalendarResponseDto> dtos = new ArrayList<>();
        for (Calendar calendar : calendarList) {
            dtos.add(CalendarResponseDto.from(calendar));
        }
        return ResponseEntity.ok(dtos);
    }

    //Update
    @Operation(
            summary = "캘린더 수정",
            description = "유저인포에 따른 캘린더를 수정합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 calendar가 맞는 경우 캘린더를 수정합니다.",
            parameters = {
                    @Parameter(name = "calendarId", description = "[path_variable] calendar의 Id", required = true)
            }
    )
    @PatchMapping("/{calendarId}")
    public ResponseEntity<CalendarResponseDto> updateCalendar(@PathVariable Long calendarId, @RequestBody CalendarRequestDto calendarRequestDto) {
        try {
            Calendar updateCalendar = calendarService.updateCalendar(calendarId, calendarRequestDto);
            return ResponseEntity.ok(CalendarResponseDto.from(updateCalendar));
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @Operation(
            summary = "캘린더 삭제",
            description = "유저인포에 따른 캘린더를 삭제합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포의 calendar가 맞는 경우 캘린더 ID로 캘린더를 삭제합니다.",
            parameters = {
                    @Parameter(name = "calendarId", description = "[path_variable] calendar의 Id", required = true)
            }
    )
    @DeleteMapping("/{calendarId}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long calendarId){
        calendarService.deleteCalendar(calendarId);
        return ResponseEntity.noContent().build();
    }


}
