package com.skuteam3.fourj.calendar.repository;

import com.skuteam3.fourj.calendar.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
