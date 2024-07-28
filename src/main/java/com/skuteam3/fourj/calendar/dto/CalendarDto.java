package com.skuteam3.fourj.calendar.dto;

import com.skuteam3.fourj.calendar.domain.Calendar;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarDto {
    private int year;
    private int month;
    private int day;

    public static CalendarDto from(Calendar calendar){
        CalendarDto calendarDto = new CalendarDto();
        calendarDto.setYear(calendar.getYear());
        calendarDto.setMonth(calendar.getMonth());
        calendarDto.setDay(calendar.getDay());
        return calendarDto;
    }
}
