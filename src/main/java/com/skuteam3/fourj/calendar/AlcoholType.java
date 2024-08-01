package com.skuteam3.fourj.calendar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum AlcoholType {

    BEER(0.05, 500),
    SOJU(0.16, 360),
    HIGHBALL(0.09, 350),
    KAOLIANG(0.4, 750);

    private final double percentage;
    private final int ml;

    AlcoholType(double percentage, int ml) {
        this.percentage = percentage * 0.7947;
        this.ml = ml;
    }

    public double getPercentage() {
        return percentage;
    }

    public int getMl() {
        return ml;
    }
}
