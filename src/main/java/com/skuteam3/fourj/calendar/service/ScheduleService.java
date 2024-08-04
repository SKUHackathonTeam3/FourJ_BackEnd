package com.skuteam3.fourj.calendar.service;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.calendar.AlcoholType;
import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.domain.Schedule;
import com.skuteam3.fourj.calendar.dto.ScheduleRequestDto;
import com.skuteam3.fourj.calendar.dto.ScheduleResponseDto;
import com.skuteam3.fourj.calendar.dto.WeeklyAlcoholSummaryDto;
import com.skuteam3.fourj.calendar.repository.CalendarRepository;
import com.skuteam3.fourj.calendar.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Schedule createSchedule(ScheduleRequestDto scheduleRequestDto, int year, int month, int day, String userEmail){

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")).getUserInfo();

        Optional<Calendar> calendarOptional = calendarRepository.findByYearAndMonthAndDayAndUserInfo(year, month, day, userInfo);
        Calendar calendar;
        if (calendarOptional.isEmpty()) {
            calendar = calendarRepository.save(new Calendar(year, month, day, userInfo));
        } else {
            calendar = calendarOptional.get();
            return updateSchedule(calendar.getSchedule().get(0).getId(), scheduleRequestDto);
        }

        Schedule schedule = new Schedule();

        schedule.setCalendar(calendar);
        schedule.setMemo(scheduleRequestDto.getMemo());
        schedule.setTodayCondition(scheduleRequestDto.getTodayCondition());
        schedule.setBeerAlcohol(scheduleRequestDto.getBeerAlcohol());
        schedule.setSojuAlcohol(scheduleRequestDto.getSojuAlcohol());
        schedule.setHighballAlcohol(scheduleRequestDto.getHighballAlcohol());
        schedule.setKaoliangAlcohol(scheduleRequestDto.getKaoliangAlcohol());
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public Schedule updateSchedule(Long id, ScheduleRequestDto scheduleRequestDto){
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(id);
        if(scheduleOptional.isPresent()){
            Schedule schedule = scheduleOptional.get();
            if(scheduleRequestDto.getMemo() != null){
                schedule.setMemo(scheduleRequestDto.getMemo());
            }
            if(scheduleRequestDto.getBeerAlcohol() != null){
                schedule.setBeerAlcohol(scheduleRequestDto.getBeerAlcohol());
            }
            if(scheduleRequestDto.getSojuAlcohol() != null){
                schedule.setSojuAlcohol(scheduleRequestDto.getSojuAlcohol());
            }
            if(scheduleRequestDto.getHighballAlcohol() != null){
                schedule.setHighballAlcohol(scheduleRequestDto.getHighballAlcohol());
            }
            if(scheduleRequestDto.getKaoliangAlcohol() != null){
                schedule.setKaoliangAlcohol(scheduleRequestDto.getKaoliangAlcohol());
            }
            schedule.setTodayCondition(scheduleRequestDto.getTodayCondition());
            return scheduleRepository.save(schedule);
        }else{
            throw new RuntimeException("Schedule not found with id" + id);
        }
    }

    @Transactional
    public void deleteSchedule(Long id){
        Optional<Schedule> scheduleOptional =scheduleRepository.findById(id);
        Schedule schedule = scheduleOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));

        Calendar calendar = schedule.getCalendar();
        List<Schedule> schedules =calendar.getSchedule();

        scheduleRepository.deleteById(id);

        if (schedules.size() == 1) {

            calendarRepository.delete(calendar);
        }
    }

    //월별 일정 조회
    public List<Schedule> getSchedulesByYearAndMonthAndUserInfo(int year, int month, String userEmail) {
        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();
        return calendarRepository.findByYearAndMonthAndUserInfo(year, month, userInfo).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found")).getSchedule();
    }
    //해당 날짜 일정 조회
    public List<Schedule> getSchedulesByYearAndMonthAndDayAndUserInfo(int year, int month, int day, String userEmail) {
        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();
        return calendarRepository.findByYearAndMonthAndDayAndUserInfo(year, month, day, userInfo).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found")).getSchedule();
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
