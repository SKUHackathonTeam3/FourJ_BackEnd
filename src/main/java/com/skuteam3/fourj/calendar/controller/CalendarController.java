package com.skuteam3.fourj.calendar.controller;

import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.dto.CalendarDto;
import com.skuteam3.fourj.calendar.service.CalendarService;
import com.skuteam3.fourj.calendar.service.ScheduleService;
import com.skuteam3.fourj.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/calendar")
public class CalendarController {

    private final JwtProvider jwtProvider;
    private final CalendarService calendarService;
    private final ScheduleService scheduleService;


    //Create
    @PostMapping
    public String createCalendar(HttpServletRequest request, @RequestBody CalendarDto calendarDto){
        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        calendarService.createCalendar(calendarDto, userEmail);
        return "Calendar created successfully";
    }

    //Read
    @GetMapping
    public ResponseEntity<CalendarDto> getCalendarByUserEmail(HttpServletRequest request){
        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        Optional<Calendar> calendarOptional = calendarService.getCalendarByUserEmail(userEmail);
        if (calendarOptional.isPresent()){
            CalendarDto dto = CalendarDto.from(calendarOptional.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Update
    @PatchMapping("/{id}")
    public ResponseEntity<CalendarDto> updateCalendar(@PathVariable Long id, @RequestBody CalendarDto calendarDto) {
        try {
            Calendar updateCalendar = calendarService.updateCalendar(id, calendarDto);
            return ResponseEntity.ok(CalendarDto.from(updateCalendar));
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long id){
        calendarService.deleteCalendar(id);
        return ResponseEntity.noContent().build();
    }


}
