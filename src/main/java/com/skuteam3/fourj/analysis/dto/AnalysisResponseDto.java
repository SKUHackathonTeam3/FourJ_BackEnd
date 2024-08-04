package com.skuteam3.fourj.analysis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.index.qual.SearchIndexBottom;
import java.util.Map;

@Getter
@Setter
public class AnalysisResponseDto {

    @Schema(description = "기본 주량을 넘긴 날의 월", example = "7")
    private int month;
    @Schema(description = "기본 주량을 넘긴 날의 일", example = "29")
    private int day;
    @Schema(description = "기본 주량을 넘긴 날의 약속 메모", example = "친구들과 일본 여행")
    private String memo;
    @Schema(description = "기본 주량을 넘긴 날의 마신 술의 양", example = "3.5")
    private double alcohol;

    public AnalysisResponseDto(int month, int day, String memo, double alcohol) {

        this.month = month;
        this.day = day;
        this.memo = memo;
        this.alcohol = alcohol;
    }


}

