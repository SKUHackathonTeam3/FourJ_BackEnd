package com.skuteam3.fourj.calendar.service;

import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.dto.CalendarDto;
import com.skuteam3.fourj.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    @Transactional
    public Calendar createCalendar(CalendarDto calendarDto, String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("user not found"));
        UserInfo userInfo = user.getUserInfo();

        Calendar calendar = new Calendar();
        calendar.setYear(calendarDto.getYear());
        calendar.setMonth(calendarDto.getMonth());
        calendar.setDay(calendarDto.getDay());
        calendar.setUserInfo(userInfo);

        return calendarRepository.save(calendar);
    }

    @Transactional
    public Calendar updateCalendar(Long id, CalendarDto calendarDto){
        Optional<Calendar> calendarOptional = calendarRepository.findById(id);
        if (calendarOptional.isPresent()) {
            Calendar calendar = calendarOptional.get();
            calendar.setYear(calendarDto.getYear());
            calendar.setMonth(calendarDto.getMonth());
            calendar.setDay(calendarDto.getDay());
            return calendarRepository.save(calendar);
        }else {
            throw new RuntimeException("Calendar not found with id" + id);
        }
    }

    @Transactional
    public void deleteCalendar(Long id){
        calendarRepository.deleteById(id);
    }

    public Optional<Calendar> getCalendarByUserEmail(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("user not found"));
        UserInfo userInfo = user.getUserInfo();

        return calendarRepository.findCalendarByUserInfo(userInfo);
    }

}
