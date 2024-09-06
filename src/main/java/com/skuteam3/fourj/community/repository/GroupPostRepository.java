package com.skuteam3.fourj.community.repository;

import com.skuteam3.fourj.community.domain.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface    GroupPostRepository extends JpaRepository<GroupPost, Long> {
    List<GroupPost> findGroupPostByHashtagContaining(String hashtag);

    List<GroupPost> findByTitleContainingOrContentsContaining(String titleKeyword, String contentKeyword);

    @Query("SELECT gp FROM GroupPost gp " +
            "LEFT JOIN gp.likes l " +
            "GROUP BY gp.id " +
            "HAVING COUNT(l.id) > 9 " +
            "ORDER BY COUNT(l.id) DESC")
    List<GroupPost> findTop5GroupPostsWithLikesGreaterThanNine();

}
