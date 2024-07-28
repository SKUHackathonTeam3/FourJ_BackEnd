package com.skuteam3.fourj.community.repository;

import com.skuteam3.fourj.community.domain.PrivatePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivatePostRepository extends JpaRepository<PrivatePost, Long> {
    List<PrivatePost> findByContentsContaining(String keyword);
}
