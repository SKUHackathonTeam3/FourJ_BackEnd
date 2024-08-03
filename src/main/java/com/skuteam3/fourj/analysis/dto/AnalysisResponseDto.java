package com.skuteam3.fourj.analysis.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.index.qual.SearchIndexBottom;
import java.util.Map;

@Getter
@Setter
public class AnalysisResponseDto {

    private int month;
    private int day;
    private String memo;
    private double alcohol;

    public AnalysisResponseDto(int month, int day, String memo, double alcohol) {

        this.month = month;
        this.day = day;
        this.memo = memo;
        this.alcohol = alcohol;
    }


}
