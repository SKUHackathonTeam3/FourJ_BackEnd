package com.skuteam3.fourj.account.repository;

import com.skuteam3.fourj.account.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
