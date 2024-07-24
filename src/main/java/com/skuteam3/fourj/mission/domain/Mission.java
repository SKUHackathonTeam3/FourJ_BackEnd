package com.skuteam3.fourj.mission.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
public class Mission {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="mission_text", nullable=false)
    private String missionText;

    @Builder
    public Mission (String missionText) {
        this.missionText = missionText;
    }
}
