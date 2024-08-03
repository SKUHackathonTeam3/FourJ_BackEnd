package com.skuteam3.fourj.calendar.repository;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findCalendarByUserInfo(UserInfo userInfo);

    Optional<Calendar> findScheduleByYearAndMonthAndUserInfo( int year,int month, UserInfo userInfo);

    Optional<Calendar> findScheduleByYearAndMonthAndDayAndUserInfo(int year, int month, int day, UserInfo userInfo);

    List<Calendar> findAllByUserInfoAndYearAndMonthAndDayBetween(UserInfo userInfo, int year, int month, int startDate, int endDate);

}
