package com.skuteam3.fourj.calendar.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Schedule_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="Calendar_id")
    private Calendar calendar;

    private String memo;
    private int todayCondition;
    @Column(columnDefinition = "FLOAT")
    private Double beerAlcohol;
    @Column(columnDefinition = "FLOAT")
    private Double sojuAlcohol;
    @Column(columnDefinition = "FLOAT")
    private Double highballAlcohol;
    @Column(columnDefinition = "FLOAT")
    private Double kaoliangAlcohol;
    @CreationTimestamp
    private LocalDateTime scheduleTime;

}
