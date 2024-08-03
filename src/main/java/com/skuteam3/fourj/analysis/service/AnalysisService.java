package com.skuteam3.fourj.analysis.service;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.analysis.dto.AnalysisResponseDto;
import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.domain.Schedule;
import com.skuteam3.fourj.calendar.repository.CalendarRepository;
import com.skuteam3.fourj.calendar.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AnalysisService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final ScheduleService scheduleService;

    // 사용자 이름
    public String getUserName(String userEmail){
        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found.")).getUserInfo();
        String userName = userInfo.getName();
        return userName;
    }

    // 주간 총 음주량
    public Map<String, Double> getWeeklyTotalAlcohol(int year, int month, int startDate, String userEmail){

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();

        LocalDate createdAt = userInfo.getCreatedAt().toLocalDate();
        LocalDate startedAt = LocalDate.of(year, month, startDate);
        LocalDate endAt= LocalDate.of(year, month, startDate).plusDays(6);
        int endDate = endAt.getDayOfMonth();

        LocalDate createdAtWeek = createdAt.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate startedAtWeek = startedAt.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        if(createdAtWeek.equals(startedAtWeek)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's analysing. startDate must be in the week after UserInfo's createAt date.");
        }

        List<Calendar> calendarList = calendarRepository.findAllByUserInfoAndBetweenDays(
                userInfo,
                year,
                month,
                startDate,
                endAt.getYear(),
                endAt.getMonthValue(),
                endAt.getDayOfMonth()
        );

        double beerAlcoholSum = 0.0;
        double sojuAlcoholSum = 0.0;
        double highballAlcoholSum = 0.0;
        double kaoliangAlcoholSum = 0.0;

        for (Calendar calendar: calendarList){
            List<Schedule> schedules = calendar.getSchedule();
            for (Schedule schedule: schedules) {
                if (schedule.getBeerAlcohol() != null) beerAlcoholSum += schedule.getBeerAlcohol();
                if (schedule.getSojuAlcohol() != null) sojuAlcoholSum += schedule.getSojuAlcohol();
                if (schedule.getHighballAlcohol() != null) highballAlcoholSum += schedule.getHighballAlcohol();
                if (schedule.getKaoliangAlcohol() != null) kaoliangAlcoholSum += schedule.getKaoliangAlcohol();
            }
        }

        Map<String, Double> weeklyTotalAlcohol = new HashMap<>();
        weeklyTotalAlcohol.put("weeklyTotalBeer", beerAlcoholSum);
        weeklyTotalAlcohol.put("weeklyTotalSoju", sojuAlcoholSum);
        weeklyTotalAlcohol.put("weeklyTotalHighball", highballAlcoholSum);
        weeklyTotalAlcohol.put("weeklyTotalkaoliang", kaoliangAlcoholSum);

        return weeklyTotalAlcohol;
    }

    // 일주일 평균 음주 예정일 ex) 3일, 해당 주만
    public Integer getWeeklyAverageAlcoholDays(int year, int month, int startDate, String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();

        // startDate가 UserInfo의 createdAt 날짜와 같은 주인 경우 분석중 메시지
        LocalDate createdAt = userInfo.getCreatedAt().toLocalDate();
        LocalDate startedAt = LocalDate.of(year, month, startDate);
        LocalDate endAt= LocalDate.of(year, month, startDate).plusDays(6);
        int endDate = endAt.getDayOfMonth();

        LocalDate createdAtWeek = createdAt.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate startedAtWeek = startedAt.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        if(createdAtWeek.equals(startedAtWeek)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's analysing. startDate must be in the week after UserInfo's createAt date.");
        }

        List<Calendar> calendarList = calendarRepository.findAllByUserInfoAndBetweenDays(
                userInfo,
                year,
                month,
                startDate,
                endAt.getYear(),
                endAt.getMonthValue(),
                endAt.getDayOfMonth()
        );
        List<Calendar> scheduledDates = new ArrayList<>();

        // 스케줄이 있는 날짜
        for(Calendar calendarDates : calendarList){
            List<Schedule> schedules = calendarDates.getSchedule();
            for(Schedule schedule : schedules){
                if (schedule.getKaoliangAlcohol() != null && schedule.getKaoliangAlcohol() != 0.0 ||
                        schedule.getHighballAlcohol() != null && schedule.getHighballAlcohol() != 0.0 ||
                        schedule.getBeerAlcohol() != null && schedule.getBeerAlcohol() != 0.0 ||
                        schedule.getSojuAlcohol() != null && schedule.getSojuAlcohol()  != 0.0) {
                    scheduledDates.add(calendarDates);
                }

            }
        }

        return (new HashSet<>(scheduledDates)).size();
    }

    // 이번주 금주의 날 ex) 3일
    public Integer getNoAlcoholDays(int year, int month, int startDate, String userEmail) {

        LocalDate endAt= LocalDate.of(year, month, startDate).plusDays(6);
        int endDate = endAt.getDayOfMonth();
        // 스케줄이 없는 날짜
        return 7 - getWeeklyAverageAlcoholDays(year, month, startDate, userEmail);
    }

    // 주간 기본 주량을 넘긴 날 ex) 8월 2일 + memo
    public List<AnalysisResponseDto> getOverredAlcoholSchedules(int year, int month, int startDate, String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();

        // startDate가 UserInfo의 createdAt 날짜와 같은 주인 경우 분석중 메시지
        LocalDate createdAt = userInfo.getCreatedAt().toLocalDate();
        LocalDate startedAt = LocalDate.of(year, month, startDate);
        LocalDate endAt= LocalDate.of(year, month, startDate).plusDays(6);

        LocalDate createdAtWeek = createdAt.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate startedAtWeek = startedAt.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        if(createdAtWeek.equals(startedAtWeek)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's analysing. startDate must be in the week after UserInfo's createAt date.");
        }

        List<Calendar> calendarList = calendarRepository.findAllByUserInfoAndBetweenDays(
                userInfo,
                year,
                month,
                startDate,
                endAt.getYear(),
                endAt.getMonthValue(),
                endAt.getDayOfMonth()
        );
        List<AnalysisResponseDto> analysisResponseDtos = new ArrayList<>();

        for(Calendar calendarDates : calendarList){
            List<Schedule> schedules = calendarDates.getSchedule();
            for (Schedule schedule : schedules) {

                Double dailyAlcoholAmount = scheduleService.calculateScheduleAlcohol(schedule.getId());

                if (schedule.getMemo() != null && userInfo.getAverageAlcoholAmount() < dailyAlcoholAmount) {
                    AnalysisResponseDto dto = new AnalysisResponseDto(calendarDates.getMonth(), calendarDates.getDay(), schedule.getMemo(), dailyAlcoholAmount);
                    analysisResponseDtos.add(dto);
                }
            }
        }

        return analysisResponseDtos;
    }


    // 절주가 필요한 상황 = 절주 챌린지가 필요한 상황
    // 일주일 평균 음주 예정일이 3일 이상
    public String getNeedReductionAlcohol(int year, int month, int startDate, String userEmail){

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();

        // startDate가 UserInfo의 createdAt 날짜와 같은 주인 경우 분석중 메시지
        LocalDate createdAt = userInfo.getCreatedAt().toLocalDate();
        LocalDate startedAt = LocalDate.of(year, month, startDate);
        LocalDate endAt= LocalDate.of(year, month, startDate).plusDays(6);
        int endDate = endAt.getDayOfMonth();

        LocalDate createdAtWeek = createdAt.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate startedAtWeek = startedAt.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        if(createdAtWeek.equals(startedAtWeek)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's analysing. startDate must be in the week after UserInfo's createAt date.");
        }

        int weeklyAverageAlcoholDates = this.getWeeklyAverageAlcoholDays(year, month, startDate, userEmail);

        if(weeklyAverageAlcoholDates >= 3){
            return "Weekly average alcohol dates: " + weeklyAverageAlcoholDates + " Need ReductionAlcoholChallenge. ";
        }else {
            return "Don't need ReductionAlcoholChallenge";
        }
    }

    // 주간 별점
    // 2일, 4일, 6일, 별 1개씩 누적 감점
    // 일주일 중 주량 넘긴 날이 25%이상 1개 감점, 50%이상 2개 감점
    public int setWeeklyStar(int year, int month, int startDate, String userEmail){

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();

        // startDate가 UserInfo의 createdAt 날짜와 같은 주인 경우 분석중 메시지
        LocalDate createdAt = userInfo.getCreatedAt().toLocalDate();
        LocalDate startedAt = LocalDate.of(year, month, startDate);
        LocalDate endAt= LocalDate.of(year, month, startDate).plusDays(6);
        int endDate = endAt.getDayOfMonth();

        LocalDate createdAtWeek = createdAt.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate startedAtWeek = startedAt.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        if(createdAtWeek.equals(startedAtWeek)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's analysing. startDate must be in the week after UserInfo's createAt date.");
        }

        int weeklyAverageAlcoholDates = this.getWeeklyAverageAlcoholDays(year, month, startDate, userEmail);
        int overredAlcoholDays = 0;



        List<Calendar> calendarList = calendarRepository.findAllByUserInfoAndBetweenDays(
                userInfo,
                year,
                month,
                startDate,
                endAt.getYear(),
                endAt.getMonthValue(),
                endAt.getDayOfMonth()
        );

        for(Calendar calendarDates : calendarList){

            List<Schedule> schedules = calendarDates.getSchedule();
            Double dailyAlcoholAmount = scheduleService.calculateWeeklyAlcohol(calendarDates.getYear(), calendarDates.getMonth(), calendarDates.getDay(), calendarDates.getDay(), userEmail);
            if (userInfo.getAverageAlcoholAmount() < dailyAlcoholAmount) {
                overredAlcoholDays ++;
            }
        }


        int star = 5;
        if(weeklyAverageAlcoholDates >= 2){
            star--;
        }
        if(weeklyAverageAlcoholDates >= 4){
            star--;
        }
        if(weeklyAverageAlcoholDates >= 6){
            star--;
        }
        if( overredAlcoholDays >= 1.75){
            star--;
        }
        if( overredAlcoholDays >= 3.5){
            star--;
        }
        return star;
    }

    // 별점 순 종합 코멘트
    public String totalComments(int star){

        if (star == 0) {
            return "1 star comments";
        } else if (star == 2) {
            return "2 star comments";
        } else if (star == 3) {
            return "3 star comments";
        } else if (star == 4) {
            return "4 star comments";
        } else if (star == 5) {
            return "5 star comments";
        } else {
            return "0 star comments";
        }
    }

}
