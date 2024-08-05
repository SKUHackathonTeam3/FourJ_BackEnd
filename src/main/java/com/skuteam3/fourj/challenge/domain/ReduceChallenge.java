package com.skuteam3.fourj.challenge.domain;

import com.skuteam3.fourj.account.domain.UserInfo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class ReduceChallenge {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "week_start", nullable = false, unique = true)
    private LocalDate weekStart;

    @Column(name = "first_week_drink_goal", nullable = false)
    private Double firstWeekDrinkGoal;

    @Column(name = "second_week_drink_goal", nullable = false)
    private Double secondWeekDrinkGoal;

    @Column(name = "achieved")
    private boolean achieved;

    @Column(name = "weekly_average_drink")
    private Double weeklyAverageDrink;
}
