package com.skuteam3.fourj.calendar.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class WeeklyAlcoholSummaryDto {

    private Double beer;
    private Double soju;
    private Double highball;
    private Double kaoliang;

    @Builder
    public WeeklyAlcoholSummaryDto(Double beer, Double soju, Double highball, Double kaoliang) {
        this.beer = beer;
        this.soju = soju;
        this.highball = highball;
        this.kaoliang = kaoliang;
    }
}
