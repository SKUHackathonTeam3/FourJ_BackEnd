package com.skuteam3.fourj.global.utils;

import com.skuteam3.fourj.mission.domain.Mission;
import com.skuteam3.fourj.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final MissionRepository missionRepository;

    @Override
    public void run(String... args) throws Exception {

        loadInitialData();
    }

    public void loadInitialData() {

        missionRepository.save(Mission.builder().
                missionText("출석체크하기").build());

        missionRepository.save(Mission.builder().
                missionText("안주 추천 받기 기능 사용하기").build());

        missionRepository.save(Mission.builder().
                missionText("주량 재설정하기").build());
    }

}
