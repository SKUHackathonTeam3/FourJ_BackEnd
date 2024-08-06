package com.skuteam3.fourj.attendance.service;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserInfoRepository;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.attendance.domain.Attendance;
import com.skuteam3.fourj.attendance.dto.AttendanceListRequestDto;
import com.skuteam3.fourj.attendance.dto.AttendanceRequestDto;
import com.skuteam3.fourj.attendance.repository.AttendanceRepository;
import com.skuteam3.fourj.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AttendanceService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final AttendanceRepository attendanceRepository;
    private final MissionService missionService;

    @Transactional
    public void checkIn(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원 정보 조회를 실패하였습니다.")).getUserInfo();
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();

        // 오늘 출석한 데이터가 이미 존재하는 경우
        List<Attendance> todayAttendance = attendanceRepository.findByUserInfoAndAttendanceDateBetween(userInfo, today, today.plusDays(1).minusNanos(1));
        if (!todayAttendance.isEmpty()) {

            if (userInfo.getLastAttendanceDate() != today.toLocalDate())
                userInfo.setLastAttendanceDate(today.toLocalDate());

            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 출석하였습니다");
        }

        // 오늘 출석한 데이터가 존재하지 않지만, 유저 인포의 최근 출석 일자가 오늘인 경우
        if (userInfo.getLastAttendanceDate() == today.toLocalDate()) {

            Attendance attendance = attendanceRepository.findFirstByUserInfoOrderByAttendanceDateDesc(userInfo).orElse(null);
            LocalDate lastAttendanceDate = null;

            if (attendance != null) lastAttendanceDate = attendance.getAttendanceDate();

            userInfo.setLastAttendanceDate(lastAttendanceDate);
            userInfoRepository.save(userInfo);

            log.warn("AttendanceService_checkIn 출석 데이터 비정상");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "출석체크를 재시도 해주세요.");
        }

        // 유저의 연속 출석일 계산
        int continuousAttendanceDays = 1;
        if (userInfo.getLastAttendanceDate() != null) {

            continuousAttendanceDays = userInfo.getContinuousAttendanceDays() + 1;
            if (continuousAttendanceDays > userInfo.getMaximumAttendanceDays()) {

                userInfo.setMaximumAttendanceDays(continuousAttendanceDays);
            }
        }

        // 출석체크 추가
        Attendance attendance = new Attendance();
        attendance.setUserInfo(userInfo);

        try {

            missionService.clearMission(1L, userEmail);
        } catch (ResponseStatusException rse) {

            throw new ResponseStatusException(rse.getStatusCode(), "Mission: " + rse.getMessage());
        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, "미션 성공 정보 저장 실패");
        }

        // 연속 출석일 및 최근 출석일 갱신
        userInfo.setContinuousAttendanceDays(continuousAttendanceDays);
        userInfo.setLastAttendanceDate(today.toLocalDate());

        attendanceRepository.save(attendance);
        userInfoRepository.save(userInfo);
    }

    @Transactional
    public AttendanceListRequestDto getWeeklyAttendance(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원 정보 조회를 실패하였습니다.")).getUserInfo();

        LocalDateTime now = LocalDateTime.now();
        LocalDate startOfWeekDate = now.toLocalDate().with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeekDate = now.toLocalDate().with(java.time.DayOfWeek.SUNDAY);

        LocalDateTime startOfWeekDateTime = startOfWeekDate.atStartOfDay();
        LocalDateTime endOfWeekDateTime = endOfWeekDate.plusDays(1).atStartOfDay().minusNanos(1);

        List<Attendance> attendanceList = attendanceRepository.findByUserInfoAndAttendanceDateBetween(
                userInfo,
                startOfWeekDateTime,
                endOfWeekDateTime);

        // 주간 출석 현황 정보를 boolean값으로 저장(boolean의 초기값은 false)
        AttendanceListRequestDto attendanceListRequestDto = new AttendanceListRequestDto();
        for (Attendance attendance: attendanceList) {

            switch (attendance.getAttendanceDate().getDayOfWeek()) {

                case MONDAY : attendanceListRequestDto.setMonday(true); break;
                case TUESDAY: attendanceListRequestDto.setTuesday(true); break;
                case WEDNESDAY: attendanceListRequestDto.setWednesday(true); break;
                case THURSDAY: attendanceListRequestDto.setThursday(true); break;
                case FRIDAY: attendanceListRequestDto.setFriday(true); break;
                case SATURDAY: attendanceListRequestDto.setSaturday(true); break;
                case SUNDAY: attendanceListRequestDto.setSunday(true); break;
            }
        }

        return attendanceListRequestDto;
    }

    public AttendanceRequestDto getContinuousAttendanceDays(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원 정보 조회를 실패하였습니다.")).getUserInfo();
        AttendanceRequestDto attendanceRequestDto = new AttendanceRequestDto();

        // 연속 출석일 정보 저장
        attendanceRequestDto.setContinuousAttendanceDays(userInfo.getContinuousAttendanceDays());
        return attendanceRequestDto;
    }
}
