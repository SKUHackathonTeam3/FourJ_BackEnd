package com.skuteam3.fourj.calendar.domain;

import com.skuteam3.fourj.account.domain.UserInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Calendar_id")
    private Long id;

    private int year;
    private int month;
    private int day;

    @OneToMany(mappedBy="calendar")
    private List<Schedule> schedule = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="User_id")
    private UserInfo userInfo;

    public Calendar(int year, int month, int day, UserInfo userInfo) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.userInfo = userInfo;
    }
}

