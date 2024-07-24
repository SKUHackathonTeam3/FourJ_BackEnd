package com.skuteam3.fourj.calendar.dto;

import com.skuteam3.fourj.calendar.domain.Schedule;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDto {
    private String memo;
    private int todayCondition;
    private Double beerAlcohol;
    private Double sojuAlcohol;
    private Double highballAlcohol;
    private Double kaoliangAlcohol;

    public static ScheduleDto from(Schedule schedule) {
        ScheduleDto scheduleDto = new ScheduleDto();

        scheduleDto.setMemo(schedule.getMemo());
        scheduleDto.setTodayCondition(schedule.getTodayCondition());
        scheduleDto.setBeerAlcohol(schedule.getBeerAlcohol());
        scheduleDto.setSojuAlcohol(schedule.getSojuAlcohol());
        scheduleDto.setHighballAlcohol(schedule.getHighballAlcohol());
        scheduleDto.setKaoliangAlcohol(schedule.getKaoliangAlcohol());
        return scheduleDto;
    }

}
