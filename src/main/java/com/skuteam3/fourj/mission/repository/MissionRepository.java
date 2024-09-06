package com.skuteam3.fourj.mission.repository;

import com.skuteam3.fourj.mission.domain.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {


}
