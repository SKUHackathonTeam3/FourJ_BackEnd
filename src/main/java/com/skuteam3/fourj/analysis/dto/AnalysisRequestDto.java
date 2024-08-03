package com.skuteam3.fourj.analysis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisRequestDto {

    private int year;
    private int month;
    private int startDate;
    private Double averageAlcoholAmount;

}
