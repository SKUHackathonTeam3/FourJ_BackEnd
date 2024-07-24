package com.skuteam3.fourj.calendar.service;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.domain.Schedule;
import com.skuteam3.fourj.calendar.dto.ScheduleDto;
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

}
