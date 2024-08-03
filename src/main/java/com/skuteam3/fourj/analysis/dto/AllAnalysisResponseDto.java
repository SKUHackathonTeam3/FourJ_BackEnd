package com.skuteam3.fourj.analysis.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Data
public class AllAnalysisResponseDto {

    private String userName;
    private Integer star;
    private Map<String, Double> weeklyTotalAlcohol;
    private List<AnalysisResponseDto> analysisResponseDtoList;
    private Integer noAlcoholDays;
    private String needChallenge;
    private Integer weeklyAverageAlcoholDays;
    private String totalComments;
}
