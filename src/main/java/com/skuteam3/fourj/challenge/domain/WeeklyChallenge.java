package com.skuteam3.fourj.challenge.domain;

import com.skuteam3.fourj.account.domain.UserInfo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class WeeklyChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "goal_date", nullable = false)
    private LocalDate goalDate;

    @Column(name = "achieved")
    private Boolean achieved;

    @ManyToOne
    @JoinColumn(name = "user_info_id", nullable = false)
    private UserInfo userInfo;

    @Builder
    public WeeklyChallenge(LocalDate startDate, LocalDate endDate, LocalDate goalDate, Boolean achieved) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.goalDate = goalDate;
        this.achieved = achieved;
    }

}
