package com.skuteam3.fourj.calendar.repository;

import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.calendar.domain.Schedule;
import com.skuteam3.fourj.calendar.dto.WeeklyAlcoholSummaryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT new com.skuteam3.fourj.calendar.dto.WeeklyAlcoholSummaryDto(" +
            "SUM(s.beerAlcohol), SUM(s.sojuAlcohol), " +
            "SUM(s.highballAlcohol), SUM(s.kaoliangAlcohol)) " +
            "FROM Schedule s WHERE s.calendar.year = :year AND s.calendar.month = :month " +
            "AND s.calendar.day BETWEEN :startDay AND :endDay AND s.calendar.userInfo = :userInfo")
    WeeklyAlcoholSummaryDto getWeeklyAlcoholSummary(int year, int month, int startDay, int endDay, UserInfo userInfo);

}
