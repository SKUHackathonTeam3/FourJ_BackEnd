package com.skuteam3.fourj.calendar.dto;

import com.skuteam3.fourj.calendar.domain.Calendar;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarRequestDto {
    @Schema(description = "캘린더 year", example = "2024")
    private int year;
    @Schema(description = "캘린더 month", example = "7")
    private int month;
    @Schema(description = "캘린더 day", example = "29")
    private int day;

    public static CalendarRequestDto from(Calendar calendar){
        CalendarRequestDto calendarRequestDto = new CalendarRequestDto();

        calendarRequestDto.setYear(calendar.getYear());
        calendarRequestDto.setMonth(calendar.getMonth());
        calendarRequestDto.setDay(calendar.getDay());
        return calendarRequestDto;
    }
}
