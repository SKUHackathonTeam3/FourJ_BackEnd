package com.skuteam3.fourj.calendar.service;

import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.dto.CalendarRequestDto;
import com.skuteam3.fourj.calendar.dto.CalendarResponseDto;
import com.skuteam3.fourj.calendar.repository.CalendarRepository;
import com.skuteam3.fourj.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final ChallengeService challengeService;

    @Transactional
    public Calendar createCalendar(CalendarRequestDto calendarRequestDto, String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("user not found"));
        UserInfo userInfo = user.getUserInfo();

        Calendar calendar = new Calendar();
        calendar.setYear(calendarRequestDto.getYear());
        calendar.setMonth(calendarRequestDto.getMonth());
        calendar.setDay(calendarRequestDto.getDay());
        calendar.setUserInfo(userInfo);

        try {

            if (challengeService.getOngoingWeeklyChallenge(userEmail) != null) {
                LocalDate endDate = LocalDate.of(calendarRequestDto.getYear(), calendarRequestDto.getMonth(), calendarRequestDto.getDay());
                challengeService.updateWeeklyChallengeEndDate(userEmail, endDate);
            }
        } catch (Exception ignored) {
        }

        return calendarRepository.save(calendar);
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

    public Optional<Calendar> getCalendarByUserEmail(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("user not found"));
        UserInfo userInfo = user.getUserInfo();

        return calendarRepository.findCalendarByUserInfo(userInfo);
    }

}
