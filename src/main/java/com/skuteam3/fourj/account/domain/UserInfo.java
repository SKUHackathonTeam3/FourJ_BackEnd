package com.skuteam3.fourj.account.domain;

import com.skuteam3.fourj.calendar.domain.Calendar;
import com.skuteam3.fourj.abti.domain.Abti;
import com.skuteam3.fourj.community.domain.GroupPost;
import com.skuteam3.fourj.community.domain.PrivatePost;
import com.skuteam3.fourj.contact.domain.Contact;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_info_id")
    private Long id;

    @Column(name = "user_info_name")
    private String name;

    @Column(name = "weekly_alcohol_amount")
    private Double weeklyAlcoholAmount;

    @Column(name = "average_alcohol_amount")
    private Double averageAlcoholAmount;

    @ColumnDefault("0")
    @Column(name = "continues_attendance_days")
    private Integer continuousAttendanceDays = 0;

    @Column(name = "last_attendance_date")
    private LocalDate lastAttendanceDate;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "userInfo", cascade = CascadeType.ALL)
    private User user;

    @Builder
    public UserInfo(String name, User user) {
        this.name = name;
        this.user = user;
    }


    // Feature 추가할 때 아래 추가하기!!!
  
    // Calendar
    @OneToOne(mappedBy = "userInfo")
    private Calendar calendar;

    // ABTI
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "abti", referencedColumnName = "id")
    private Abti abti;

    // Contacts
    @OneToMany(mappedBy = "userInfo")
    List<Contact> contacts  = new ArrayList<>();

    // GroupPost
    @OneToMany(mappedBy = "userInfo")
    List<GroupPost> groupPosts  = new ArrayList<>();

    //PrivatePost
    @OneToMany(mappedBy = "userInfo")
    List<PrivatePost> privatePosts  = new ArrayList<>();
}
