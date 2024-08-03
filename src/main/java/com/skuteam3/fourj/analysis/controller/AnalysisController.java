package com.skuteam3.fourj.analysis.controller;

import com.skuteam3.fourj.analysis.dto.AllAnalysisResponseDto;
import com.skuteam3.fourj.analysis.dto.AnalysisRequestDto;
import com.skuteam3.fourj.analysis.dto.AnalysisResponseDto;
import com.skuteam3.fourj.analysis.service.AnalysisService;
import com.skuteam3.fourj.calendar.service.ScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

@Tag(name = "analysis", description = "통계분석 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;
    @GetMapping
    public ResponseEntity<?> getAnalysisByUserInfoAndYearMonthAndDayRange(Authentication authentication){
        String userEmail;
        if (authentication != null)
             userEmail = authentication.getName();
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        LocalDate today = LocalDate.now();
        LocalDate weekMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate previousWeekMonday = weekMonday.minusWeeks(1);

        int year = previousWeekMonday.getYear();
        int month = previousWeekMonday.getMonthValue();
        int startDate = previousWeekMonday.getDayOfMonth();

        List<AnalysisResponseDto> analysisResponseDtos = analysisService.getOverredAlcoholSchedules(year, month, startDate, userEmail);
        AllAnalysisResponseDto allAnalysisResponseDto = new AllAnalysisResponseDto();

        try {

            allAnalysisResponseDto.setAnalysisResponseDtoList(analysisResponseDtos);
            allAnalysisResponseDto.setUserName(analysisService.getUserName(userEmail));
            allAnalysisResponseDto.setStar(analysisService.setWeeklyStar(year, month, startDate, userEmail));
            if (!analysisResponseDtos.isEmpty())
                allAnalysisResponseDto.setAnalysisResponseDtoList(analysisResponseDtos);

            allAnalysisResponseDto.setWeeklyTotalAlcohol(analysisService.getWeeklyTotalAlcohol(year, month, startDate, userEmail));

            allAnalysisResponseDto.setNoAlcoholDays(analysisService.getNoAlcoholDays(year, month, startDate, userEmail));
            allAnalysisResponseDto.setWeeklyAverageAlcoholDays(analysisService.getWeeklyAverageAlcoholDays(year, month, startDate, userEmail));
            allAnalysisResponseDto.setNeedChallenge(analysisService.getNeedReductionAlcohol(year, month, startDate, userEmail));
            allAnalysisResponseDto.setTotalComments(analysisService.totalComments(analysisService.setWeeklyStar(year, month, startDate, userEmail)));
        } catch (ResponseStatusException rse) {
            return ResponseEntity.status(rse.getStatusCode()).body(rse.getMessage());
        }

        return ResponseEntity.ok(allAnalysisResponseDto);
    }

}
