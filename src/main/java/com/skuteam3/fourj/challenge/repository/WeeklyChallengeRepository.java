package com.skuteam3.fourj.challenge.repository;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.challenge.domain.WeeklyChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyChallengeRepository extends JpaRepository<WeeklyChallenge, Long> {

    Optional<WeeklyChallenge> findByStartDateBeforeAndGoalDateAfterAndEndDateIsNullAndUserInfo(LocalDate startDate, LocalDate goalDate, UserInfo userInfo);
    Optional<WeeklyChallenge> findByAchievedIsNullAndUserInfo(UserInfo userInfo);

    @Query("Select * from WeeklyChallenge where achieved is not null and user_info_id == userInfo")
    Optional<WeeklyChallenge> arilove(UserInfo userInfo);

    boolean existsByAchievedIsTrueAndUserInfo(UserInfo userInfo);
}
