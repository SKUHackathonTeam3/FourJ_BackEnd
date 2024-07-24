package com.skuteam3.fourj.calendar.repository;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    public Optional<Calendar> findCalendarByUserInfo(UserInfo userInfo);

    Optional<Calendar> findScheduleByYearAndMonthAndUserInfo( int year,int month, UserInfo userInfo);

    Optional<Calendar> findScheduleByYearAndMonthAndDayAndUserInfo(int year, int month, int day, UserInfo userInfo);
}
