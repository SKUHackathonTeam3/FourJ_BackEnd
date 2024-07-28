package com.skuteam3.fourj.abti.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Abti {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String detail;

    @Column(name="improving_description1")
    private String improvingDescription1;

    @Column(name="improving_description2")
    private String improvingDescription2;

    @Column(name="improving_description3")
    private String improvingDescription3;

    @Builder
    public Abti(String title, String description, String detail, String improvingDescription1, String improvingDescription2, String improvingDescription3) {
        this.title = title;
        this.description = description;
        this.detail = detail;
        this.improvingDescription1 = improvingDescription1;
        this.improvingDescription2 = improvingDescription2;
        this.improvingDescription3 = improvingDescription3;
    }

}
