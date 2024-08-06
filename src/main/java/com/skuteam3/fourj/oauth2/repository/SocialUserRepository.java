package com.skuteam3.fourj.oauth2.repository;

import com.skuteam3.fourj.oauth2.domain.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {

    List<SocialUser> findByEmail(String email);
}
