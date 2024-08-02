package com.skuteam3.fourj.attendance.repository;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByUserInfoAndAttendanceDateBetween(UserInfo userInfo, LocalDateTime startDate, LocalDateTime endDate);
    Optional<Attendance> findFirstByUserInfoOrderByAttendanceDateDesc(UserInfo userInfo);
}
