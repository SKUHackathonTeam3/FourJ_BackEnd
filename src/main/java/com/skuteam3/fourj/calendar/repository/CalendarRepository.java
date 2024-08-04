package com.skuteam3.fourj.calendar.repository;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findCalendarByUserInfo(UserInfo userInfo);

    Optional<Calendar> findByYearAndMonthAndUserInfo( int year,int month, UserInfo userInfo);

    Optional<Calendar> findByYearAndMonthAndDayAndUserInfo(int year, int month, int day, UserInfo userInfo);

    @Query("SELECT e FROM Calendar e WHERE e.userInfo = :userInfo AND (e.year = :startYear AND e.month = :startMonth AND e.day >= :startDay) OR (e.year = :endYear AND e.month = :endMonth AND e.day <= :endDay) OR (e.year > :startYear AND e.year < :endYear)")
    List<Calendar> findAllByUserInfoAndBetweenDays(UserInfo userInfo, int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay);

}
