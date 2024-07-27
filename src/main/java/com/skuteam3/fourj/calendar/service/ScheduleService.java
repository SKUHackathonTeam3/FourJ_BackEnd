package com.skuteam3.fourj.calendar.service;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.calendar.AlcoholType;
import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.domain.Schedule;
import com.skuteam3.fourj.calendar.dto.ScheduleDto;
import com.skuteam3.fourj.calendar.dto.WeeklyAlcoholSummaryDto;
import com.skuteam3.fourj.calendar.repository.CalendarRepository;
import com.skuteam3.fourj.calendar.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Schedule createSchedule(ScheduleDto scheduleDto, Long calendar){
        Schedule schedule = new Schedule();

        schedule.setCalendar(calendarRepository.findById(calendar).orElseThrow(null));
        schedule.setMemo(scheduleDto.getMemo());
        schedule.setTodayCondition(scheduleDto.getTodayCondition());
        schedule.setBeerAlcohol(scheduleDto.getBeerAlcohol());
        schedule.setSojuAlcohol(scheduleDto.getSojuAlcohol());
        schedule.setHighballAlcohol(scheduleDto.getHighballAlcohol());
        schedule.setKaoliangAlcohol(scheduleDto.getKaoliangAlcohol());
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public Schedule updateSchedule(Long id, ScheduleDto scheduleDto){
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(id);
        if(scheduleOptional.isPresent()){
            Schedule schedule = scheduleOptional.get();
            if(scheduleDto.getMemo() != null){
                scheduleDto.setMemo(scheduleDto.getMemo());
            }
            if(scheduleDto.getBeerAlcohol() != null){
                schedule.setBeerAlcohol(scheduleDto.getBeerAlcohol());
            }
            if(scheduleDto.getSojuAlcohol() != null){
                schedule.setSojuAlcohol(scheduleDto.getSojuAlcohol());
            }
            if(scheduleDto.getHighballAlcohol() != null){
                schedule.setHighballAlcohol(scheduleDto.getHighballAlcohol());
            }
            if(scheduleDto.getKaoliangAlcohol() != null){
                schedule.setKaoliangAlcohol(scheduleDto.getKaoliangAlcohol());
            }
            schedule.setTodayCondition(scheduleDto.getTodayCondition());
            return scheduleRepository.save(schedule);
        }else{
            throw new RuntimeException("Schedule not found with id" + id);
        }
    }

    @Transactional
    public void deleteSchedule(Long id){
        scheduleRepository.deleteById(id);
    }

    //월별 일정 조회
    public List<Schedule> getSchedulesByYearAndMonthAndUserInfo(int year, int month, String userEmail) {
        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();
        return calendarRepository.findScheduleByYearAndMonthAndUserInfo(year, month, userInfo).orElseThrow().getSchedule();
    }
    //해당 날짜 일정 조회
    public List<Schedule> getSchedulesByYearAndMonthAndDayAndUserInfo(int year, int month, int day, String userEmail) {
        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();
        return calendarRepository.findScheduleByYearAndMonthAndDayAndUserInfo(year, month, day, userInfo).orElseThrow().getSchedule();
    }

    public Double calculateWeeklyAlcohol(int year, int month, int startDate, int endDate, String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();

        WeeklyAlcoholSummaryDto weeklyAlcoholSummary = scheduleRepository.getWeeklyAlcoholSummary(year, month, startDate, endDate, userInfo);
        Double totalBeer = 0.0;
        Double totalSoju = 0.0;
        Double totalHighball = 0.0;
        Double totalKaoliang = 0.0;

        if (weeklyAlcoholSummary.getBeer() != null) {
            totalBeer = AlcoholType.BEER.getPercentage() * AlcoholType.BEER.getMl() * weeklyAlcoholSummary.getBeer();
        }
        if (weeklyAlcoholSummary.getSoju() != null) {
            totalSoju = AlcoholType.SOJU.getPercentage() * AlcoholType.SOJU.getMl() * weeklyAlcoholSummary.getSoju();
        }
        if (weeklyAlcoholSummary.getHighball() != null) {
            totalHighball = AlcoholType.HIGHBALL.getPercentage() * AlcoholType.HIGHBALL.getMl() * weeklyAlcoholSummary.getHighball();
        }
        if (weeklyAlcoholSummary.getKaoliang() != null) {
            totalKaoliang = AlcoholType.KAOLIANG.getPercentage() * AlcoholType.KAOLIANG.getMl() * weeklyAlcoholSummary.getKaoliang();
        }

        if ((totalBeer + totalSoju + totalHighball + totalKaoliang) == 0.0){
            return 0.0;
        }

        Double totalAlcohol = (totalBeer + totalSoju + totalHighball + totalKaoliang) / (AlcoholType.SOJU.getPercentage() * AlcoholType.SOJU.getMl());

        return Math.round(totalAlcohol * 10) / 10.0;

    }

}
