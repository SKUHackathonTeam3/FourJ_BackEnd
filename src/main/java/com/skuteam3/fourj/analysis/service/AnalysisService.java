package com.skuteam3.fourj.analysis.service;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.analysis.dto.AnalysisResponseDto;
import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.calendar.domain.Schedule;
import com.skuteam3.fourj.calendar.repository.CalendarRepository;
import com.skuteam3.fourj.calendar.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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
        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();
        String userName = userInfo.getName();
        return userName;
    }

    // 주간 총 음주량
    public Map<String, Double> getWeeklyTotalAlcohol(int year, int month, int startDate, int endDate, String userEmail){

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();
        List<Calendar> calendarList = calendarRepository.findAllByUserInfoAndYearAndMonthAndDayBetween(userInfo, year, month, startDate, endDate);

        // startDate가 UserInfo의 createdAt 날짜와 같은 주인 경우 분석중 메시지
        LocalDate createdAt = userInfo.getCreatedAt().toLocalDate();
        LocalDate startedAt = LocalDate.of(year, month, startDate);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int createdAtWeek = createdAt.get(weekFields.weekOfYear());
        int startedAtWeek = startedAt.get(weekFields.weekOfYear());

        if(createdAt.getYear() == year && createdAtWeek == startedAtWeek){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's analysing. startDate must be in the week after UserInfo's createAt date.");
        }

        Double beerAlcoholSum = 0.0;
        Double sojuAlcoholSum = 0.0;
        Double highballAlcoholSum = 0.0;
        Double kaoliangAlcoholSum = 0.0;

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
    public Integer getWeeklyAverageAlcoholDays(int year, int month, int startDate, int endDate, String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();

        // startDate가 UserInfo의 createdAt 날짜와 같은 주인 경우 분석중 메시지
        LocalDate createdAt = userInfo.getCreatedAt().toLocalDate();
        LocalDate startedAt = LocalDate.of(year, month, startDate);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int createdAtWeek = createdAt.get(weekFields.weekOfYear());
        int startedAtWeek = startedAt.get(weekFields.weekOfYear());

        if(createdAt.getYear() == year && createdAtWeek == startedAtWeek){
            throw new IllegalArgumentException("It's analysing. startDate must be in the week after UserInfo's createAt date.");
        }

        List<Calendar> calendarList = calendarRepository.findAllByUserInfoAndYearAndMonthAndDayBetween(userInfo, year, month, startDate, endDate);
        List<Calendar> scheduledDates = new ArrayList<>();

        for(Calendar calendarDates : calendarList){
            List<Schedule> schedules = calendarDates.getSchedule();
            for(Schedule schedule : schedules){
                Calendar calendar = schedule.getCalendar();
                scheduledDates.add(calendar);
            }
        }

        Integer daysWithScheduleCount = 0;
        for (Calendar date : calendarList) {
            if (scheduledDates.contains(date)) {
                daysWithScheduleCount++;
            }
        }

        return daysWithScheduleCount;
    }

    // 이번주 금주의 날 ex) 3일
    public Integer getNoAlcoholDays(int year, int month, int startDate, int endDate, String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();

        // startDate가 UserInfo의 createdAt 날짜와 같은 주인 경우 분석중 메시지
        LocalDate createdAt = userInfo.getCreatedAt().toLocalDate();
        LocalDate startedAt = LocalDate.of(year, month, startDate);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int createdAtWeek = createdAt.get(weekFields.weekOfYear());
        int startedAtWeek = startedAt.get(weekFields.weekOfYear());

        if(createdAt.getYear() == year && createdAtWeek == startedAtWeek){
            throw new IllegalArgumentException("It's analysing. startDate must be in the week after UserInfo's createAt date.");
        }

        List<Calendar> calendarList = calendarRepository.findAllByUserInfoAndYearAndMonthAndDayBetween(userInfo, year, month, startDate, endDate);
        List<Calendar> scheduledDates = new ArrayList<>();

        // 스케줄이 있는 날짜
        for(Calendar calendarDates : calendarList){
            List<Schedule> schedules = calendarDates.getSchedule();
            for(Schedule schedule : schedules){
                Calendar calendar = schedule.getCalendar();
                scheduledDates.add(calendar);
            }
        }

        // 스케줄이 없는 날짜
        Integer daysWithoutScheduleCount = 0;
        for (Calendar date : calendarList) {
            if (!scheduledDates.contains(date)) {
                daysWithoutScheduleCount++;
            }
        }

        return daysWithoutScheduleCount;
    }

    // 주간 기본 주량을 넘긴 날 ex) 8월 2일 + memo
    public List<AnalysisResponseDto> getOverredAlcoholSchedules(int year, int month, int startDate, int endDate, String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();

        // startDate가 UserInfo의 createdAt 날짜와 같은 주인 경우 분석중 메시지
        LocalDate createdAt = userInfo.getCreatedAt().toLocalDate();
        LocalDate startedAt = LocalDate.of(year, month, startDate);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int createdAtWeek = createdAt.get(weekFields.weekOfYear());
        int startedAtWeek = startedAt.get(weekFields.weekOfYear());

        if(createdAt.getYear() == year && createdAtWeek == startedAtWeek){
            throw new IllegalArgumentException("It's analysing. startDate must be in the week after UserInfo's createAt date.");
        }

        List<Calendar> calendarList = calendarRepository.findAllByUserInfoAndYearAndMonthAndDayBetween(userInfo, year, month, startDate, endDate);
        List<AnalysisResponseDto> analysisResponseDtos = new ArrayList<>();
        Double dailyAlcoholAmount = scheduleService.calculateWeeklyAlcohol(year, month, startDate, endDate, userEmail);

        for(Calendar calendarDates : calendarList){
            List<Schedule> schedules = calendarDates.getSchedule();
            for (Schedule schedule : schedules) {
                if (schedule.getMemo() != null && userInfo.getAverageAlcoholAmount() < dailyAlcoholAmount) {
                    Calendar calendar = schedule.getCalendar();
                    AnalysisResponseDto dto = new AnalysisResponseDto(calendar.getMonth(), calendar.getDay(), schedule.getMemo());
                    analysisResponseDtos.add(dto);
                }
            }
        }
        return analysisResponseDtos;
    }


    // 절주가 필요한 상황 = 절주 챌린지가 필요한 상황
    // 일주일 평균 음주 예정일이 3일 이상
    public String getNeedReductionAlcohol(int year, int month, int startDate, int endDate, String userEmail){

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();

        // startDate가 UserInfo의 createdAt 날짜와 같은 주인 경우 분석중 메시지
        LocalDate createdAt = userInfo.getCreatedAt().toLocalDate();
        LocalDate startedAt = LocalDate.of(year, month, startDate);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int createdAtWeek = createdAt.get(weekFields.weekOfYear());
        int startedAtWeek = startedAt.get(weekFields.weekOfYear());

        if(createdAt.getYear() == year && createdAtWeek == startedAtWeek){
            throw new IllegalArgumentException("It's analysing. startDate must be in the week after UserInfo's createAt date.");
        }

        int weeklyAverageAlcoholDates = this.getWeeklyAverageAlcoholDays(year, month, startDate, endDate, userEmail);

        if(weeklyAverageAlcoholDates >= 3){
            return "Weekly average alcohol dates: " + weeklyAverageAlcoholDates + " Need ReductionAlcoholChallenge. ";
        }else {
            return "Don't need ReductionAlcoholChallenge";
        }
    }

    // 주간 별점
    // 2일, 4일, 6일, 별 1개씩 누적 감점
    // 일주일 중 주량 넘긴 날이 25%이상 1개 감점, 50%이상 2개 감점
    public int setWeeklyStar(int year, int month, int startDate, int endDate, String userEmail){

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow().getUserInfo();

        // startDate가 UserInfo의 createdAt 날짜와 같은 주인 경우 분석중 메시지
        LocalDate createdAt = userInfo.getCreatedAt().toLocalDate();
        LocalDate startedAt = LocalDate.of(year, month, startDate);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int createdAtWeek = createdAt.get(weekFields.weekOfYear());
        int startedAtWeek = startedAt.get(weekFields.weekOfYear());

        if(createdAt.getYear() == year && createdAtWeek == startedAtWeek){
            throw new IllegalArgumentException("It's analysing. startDate must be in the week after UserInfo's createAt date.");
        }

        List<AnalysisResponseDto> analysisResponseDtos = new ArrayList<>();
        Double dailyAlcoholAmount = scheduleService.calculateWeeklyAlcohol(year, month, startDate, endDate, userEmail);
        int weeklyAverageAlcoholDates = this.getWeeklyAverageAlcoholDays(year, month, startDate, endDate, userEmail);
        int overredAlcoholDays = 0;

        // 주량을 넘긴 날 카운트
        if (userInfo.getAverageAlcoholAmount() < dailyAlcoholAmount) {
            overredAlcoholDays++;
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
        JSONObject json = new JSONObject();

        if (star == 1) {
            json.put("totalComments", "1 star comments");
        } else if (star == 2) {
            json.put("totalComments", "2 star comments");
        } else if (star == 3) {
            json.put("totalComments", "3 star comments");
        } else if (star == 4) {
            json.put("totalComments", "4 star comments");
        } else if (star == 5) {
            json.put("totalComments", "5 star comments");
        } else {
            json.put("totalComments", "0 star comments");
        }

        return json.toString();
    }

}
