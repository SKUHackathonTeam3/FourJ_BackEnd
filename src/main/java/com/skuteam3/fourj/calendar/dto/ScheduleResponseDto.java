package com.skuteam3.fourj.calendar.dto;

import com.skuteam3.fourj.calendar.domain.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleResponseDto {
    @Schema(description = "약속 ID", example = "1")
    private Long id;
    @Schema(description = "약속 메모", example = "동아리 회식 자리")
    private String memo;
    @Schema(description = "오늘의 컨티션", example = "3")
    private int todayCondition;
    @Schema(description = "맥주 예상 음주량", example = "0.5")
    private Double beerAlcohol;
    @Schema(description = "소주 예상 음주량", example = "0.5")
    private Double sojuAlcohol;
    @Schema(description = "하이볼 예상 음주량", example = "0.5")
    private Double highballAlcohol;
    @Schema(description = "고량주 예상 음주량", example = "0.5")
    private Double kaoliangAlcohol;
    @Schema(description = "캘린더 ID", example = "1")
    private Long calendarId;

    public static ScheduleResponseDto from(Schedule schedule) {
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto();

        scheduleResponseDto.setId(schedule.getId());
        scheduleResponseDto.setMemo(schedule.getMemo());
        scheduleResponseDto.setTodayCondition(schedule.getTodayCondition());
        scheduleResponseDto.setBeerAlcohol(schedule.getBeerAlcohol());
        scheduleResponseDto.setSojuAlcohol(schedule.getSojuAlcohol());
        scheduleResponseDto.setHighballAlcohol(schedule.getHighballAlcohol());
        scheduleResponseDto.setKaoliangAlcohol(schedule.getKaoliangAlcohol());
        scheduleResponseDto.setCalendarId(schedule.getCalendar().getId());
        return scheduleResponseDto;
    }

}
