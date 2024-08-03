package com.skuteam3.fourj.calendar.domain;

import com.skuteam3.fourj.account.domain.UserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "calendar", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"year", "month", "day", "User_id"})
})
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

}
