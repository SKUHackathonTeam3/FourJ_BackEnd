package com.skuteam3.fourj.calendar.dto;

import com.skuteam3.fourj.calendar.domain.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleRequestDto {
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
    @Schema(description = "약속 시간")
    private LocalDateTime scheduleTime;

    public static ScheduleRequestDto from(Schedule schedule) {
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto();

        scheduleRequestDto.setMemo(schedule.getMemo());
        scheduleRequestDto.setTodayCondition(schedule.getTodayCondition());
        scheduleRequestDto.setBeerAlcohol(schedule.getBeerAlcohol());
        scheduleRequestDto.setSojuAlcohol(schedule.getSojuAlcohol());
        scheduleRequestDto.setHighballAlcohol(schedule.getHighballAlcohol());
        scheduleRequestDto.setKaoliangAlcohol(schedule.getKaoliangAlcohol());
        scheduleRequestDto.setScheduleTime(schedule.getScheduleTime());
        return scheduleRequestDto;
    }
}
