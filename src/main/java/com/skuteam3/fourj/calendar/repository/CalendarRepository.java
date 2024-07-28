package com.skuteam3.fourj.calendar.repository;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    public Optional<Calendar> findCalendarByUserInfo(UserInfo userInfo);

    Optional<Calendar> findScheduleByYearAndMonthAndUserInfo( int year,int month, UserInfo userInfo);

    Optional<Calendar> findScheduleByYearAndMonthAndDayAndUserInfo(int year, int month, int day, UserInfo userInfo);

}
