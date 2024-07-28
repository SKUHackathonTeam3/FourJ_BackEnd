package com.skuteam3.fourj.account.repository;

import com.skuteam3.fourj.abti.domain.Abti;
import com.skuteam3.fourj.account.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    List<UserInfo> findByAbti(Abti abti);
}
