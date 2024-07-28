package com.skuteam3.fourj.calendar.controller;

import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.domain.Schedule;
import com.skuteam3.fourj.calendar.dto.ScheduleDto;
import com.skuteam3.fourj.calendar.service.CalendarService;
import com.skuteam3.fourj.calendar.service.ScheduleService;
import com.skuteam3.fourj.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    private final JwtProvider jwtProvider;
    private final ScheduleService scheduleService;
    private final CalendarService calendarService;

    //Create
    @PostMapping("/{calendar}")
    public String createSchedule(@PathVariable Long calendar, @RequestBody ScheduleDto scheduleDto){

        scheduleService.createSchedule(scheduleDto, calendar);
        return "Schedule created successfully";
    }

    //Read
    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<ScheduleDto>> getSchedulesByYearAndMonthAndUserInfo(HttpServletRequest request, @PathVariable int year, @PathVariable int month) {
        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        List<Schedule> schedules = scheduleService.getSchedulesByYearAndMonthAndUserInfo(year, month, userEmail);
        List<ScheduleDto> scheduleDtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleDto dto = ScheduleDto.from(schedule);
            scheduleDtos.add(dto);
        }
        return ResponseEntity.ok(scheduleDtos);
    }

    @GetMapping("/{year}/{month}/{day}")
    public ResponseEntity<List<ScheduleDto>> getSchedulesByYearAndMonthAndDayAndUserInfo(HttpServletRequest request, @PathVariable int year, @PathVariable int month, @PathVariable int day){
        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        List<Schedule> schedules = scheduleService.getSchedulesByYearAndMonthAndDayAndUserInfo(year, month, day, userEmail);
        List<ScheduleDto> scheduleDtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleDto dto = ScheduleDto.from(schedule);
            scheduleDtos.add(dto);
        }
        return ResponseEntity.ok(scheduleDtos);
    }

    //Update
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable Long id, @RequestBody ScheduleDto scheduleDto) {
        try {
            Schedule updateSchedule = scheduleService.updateSchedule(id, scheduleDto);
            return ResponseEntity.ok(ScheduleDto.from(updateSchedule));
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id){
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{year}/{month}/{startDay}/{endDay}")
    public ResponseEntity<Double> calculateWeeklyAlcohol(HttpServletRequest request, @PathVariable int year, @PathVariable int month, @PathVariable int startDay, @PathVariable int endDay){

        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        Double weeklyAlcohol = scheduleService.calculateWeeklyAlcohol(year, month, startDay, endDay, userEmail);
        return ResponseEntity.ok(weeklyAlcohol);
    }


}
