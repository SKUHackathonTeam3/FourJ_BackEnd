package com.skuteam3.fourj.analysis.controller;

import com.skuteam3.fourj.analysis.dto.AllAnalysisResponseDto;
import com.skuteam3.fourj.analysis.dto.AnalysisRequestDto;
import com.skuteam3.fourj.analysis.dto.AnalysisResponseDto;
import com.skuteam3.fourj.analysis.service.AnalysisService;
import com.skuteam3.fourj.calendar.service.ScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
;

import java.util.List;
import java.util.Map;

@Tag(name = "analysis", description = "통계분석 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;
    @GetMapping("/{year}/{month}/{startDate}/{endDate}")
    public ResponseEntity<?> getAnalysisByUserInfoAndYearMonthAndDayRange(Authentication authentication, @PathVariable int year, @PathVariable int month, @PathVariable int startDate, @PathVariable int endDate, @RequestBody AnalysisRequestDto analysisRequestDto){
        String userEmail = authentication.getName();
        List<AnalysisResponseDto> analysisResponseDtos = analysisService.getOverredAlcoholSchedules(year, month, startDate, endDate, userEmail);

        AllAnalysisResponseDto allAnalysisResponseDto = new AllAnalysisResponseDto();
        allAnalysisResponseDto.setAnalysisResponseDtoList(analysisResponseDtos);
        allAnalysisResponseDto.setUserName(analysisService.getUserName(userEmail));
        allAnalysisResponseDto.setStar(analysisService.setWeeklyStar(year, month, startDate, endDate, userEmail));

        try {
            allAnalysisResponseDto.setWeeklyTotalAlcohol(analysisService.getWeeklyTotalAlcohol(year, month, startDate, endDate, userEmail));
        } catch (ResponseStatusException rse) {
            return ResponseEntity.status(rse.getStatusCode()).body(rse.getMessage());
        }
        allAnalysisResponseDto.setNoAlcoholDays(analysisService.getNoAlcoholDays(year, month, startDate, endDate, userEmail));
        allAnalysisResponseDto.setWeeklyAverageAlcoholDays(analysisService.getWeeklyAverageAlcoholDays(year, month, startDate, endDate, userEmail));
        allAnalysisResponseDto.setNeedChallenge(analysisService.getNeedReductionAlcohol(year, month, startDate, endDate, userEmail));
        allAnalysisResponseDto.setTotalComments(analysisService.totalComments(analysisService.setWeeklyStar(year, month, startDate, endDate, userEmail)));

        return ResponseEntity.ok(allAnalysisResponseDto);
    }

}
