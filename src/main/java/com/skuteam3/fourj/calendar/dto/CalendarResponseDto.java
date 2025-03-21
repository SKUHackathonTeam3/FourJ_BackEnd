package com.skuteam3.fourj.calendar.dto;

import com.skuteam3.fourj.calendar.domain.Calendar;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarResponseDto {
    @Schema(description = "캘린더 ID", example = "1")
    private Long id;
    @Schema(description = "캘린더 year", example = "2024")
    private int year;
    @Schema(description = "캘린더 month", example = "7")
    private int month;
    @Schema(description = "캘린더 day", example = "29")
    private int day;

    public static CalendarResponseDto from(Calendar calendar){
        CalendarResponseDto calendarResponseDto = new CalendarResponseDto();

        calendarResponseDto.setId(calendar.getId());
        calendarResponseDto.setYear(calendar.getYear());
        calendarResponseDto.setMonth(calendar.getMonth());
        calendarResponseDto.setDay(calendar.getDay());
        return calendarResponseDto;
    }
}
