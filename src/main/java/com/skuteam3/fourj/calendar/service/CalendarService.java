package com.skuteam3.fourj.calendar.service;

import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.dto.CalendarRequestDto;
import com.skuteam3.fourj.calendar.repository.CalendarRepository;
import com.skuteam3.fourj.challenge.dto.WeeklyChallengeResponseDto;
import com.skuteam3.fourj.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final ChallengeService challengeService;

    public void createCalendar(CalendarRequestDto calendarRequestDto, String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("user not found"));
        UserInfo userInfo = user.getUserInfo();

        try {

            WeeklyChallengeResponseDto dto = challengeService.getOngoingWeeklyChallenge(userEmail);
            if (dto != null) {
                if (dto.isReduce()) {
                    LocalDate endDate = LocalDate.of(calendarRequestDto.getYear(), calendarRequestDto.getMonth(), calendarRequestDto.getDay());

                }
                LocalDate endDate = LocalDate.of(calendarRequestDto.getYear(), calendarRequestDto.getMonth(), calendarRequestDto.getDay());
                challengeService.updateWeeklyChallengeEndDate(userEmail, endDate);
            }
        } catch (Exception ignored) {
        }

        try{
            Calendar calendar = new Calendar();
            calendar.setYear(calendarRequestDto.getYear());
            calendar.setMonth(calendarRequestDto.getMonth());
            calendar.setDay(calendarRequestDto.getDay());
            calendar.setUserInfo(userInfo);

            calendarRepository.save(calendar);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "해당 날짜의 캘린더가 이미 존재합니다.");
        }



    }

    @Transactional
    public Calendar updateCalendar(Long id, CalendarRequestDto calendarRequestDto){
        Optional<Calendar> calendarOptional = calendarRepository.findById(id);
        if (calendarOptional.isPresent()) {
            Calendar calendar = calendarOptional.get();
            calendar.setYear(calendarRequestDto.getYear());
            calendar.setMonth(calendarRequestDto.getMonth());
            calendar.setDay(calendarRequestDto.getDay());
            return calendarRepository.save(calendar);
        }else {
            throw new RuntimeException("Calendar not found with id" + id);
        }
    }

    @Transactional
    public void deleteCalendar(Long id){
        calendarRepository.deleteById(id);
    }

    public List<Calendar> getCalendarByUserEmail(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("user not found"));
        UserInfo userInfo = user.getUserInfo();

        return calendarRepository.findCalendarByUserInfo(userInfo);
    }

}
