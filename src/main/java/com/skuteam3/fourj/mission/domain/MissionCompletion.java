package com.skuteam3.fourj.mission.domain;

import com.skuteam3.fourj.account.domain.UserInfo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class MissionCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_info_id", nullable = false)
    private UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(name = "cleared_at")
    @CreationTimestamp
    private LocalDateTime clearedAt;

    @Builder
    public MissionCompletion(UserInfo userInfo, Mission mission) {
        this.userInfo = userInfo;
        this.mission = mission;
    }
}