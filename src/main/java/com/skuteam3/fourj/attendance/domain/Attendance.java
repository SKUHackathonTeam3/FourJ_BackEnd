package com.skuteam3.fourj.attendance.domain;

import com.skuteam3.fourj.account.domain.UserInfo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_info_id", nullable = false)
    private UserInfo userInfo;

    @CreationTimestamp
    @Column(name="attendance_date", nullable = false)
    private LocalDateTime attendanceDate;

    public LocalDate getAttendanceDate() {
        return attendanceDate.toLocalDate();
    }
}
