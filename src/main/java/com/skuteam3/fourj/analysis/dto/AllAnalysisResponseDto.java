package com.skuteam3.fourj.analysis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Data
public class AllAnalysisResponseDto {

    @Schema(description = "통계분석 사용자 이름", example = "김서경")
    private String userName;
    @Schema(description = "통계분석 주간 별점", example = "3")
    private Integer star;
    @Schema(description = "통계분석 주간 총 마신 술의 양", example = "weeklyTotalBeer : 1")
    private Map<String, Double> weeklyTotalAlcohol;
    @Schema(description = "통계분석 기본 주량을 넘긴 날의 월, 일, 약속 메모")
    private List<AnalysisResponseDto> analysisResponseDtoList;
    @Schema(description = "통계분석 이번주 금주의 날", example = "3")
    private Integer noAlcoholDays;
    @Schema(description = "통계분석 절주 챌린지 필요 여부", example = "Weekly average alcohol dates: 3 Need ReductionAlcoholChallenge.")
    private String needChallenge;
    @Schema(description = "통계분석 주간 술을 마신 날", example = "3")
    private Integer weeklyAverageAlcoholDays;
    @Schema(description = "통계분석 별점에 따른 종합 코멘트", example = "3 star comments")
    private String totalComments;
}
