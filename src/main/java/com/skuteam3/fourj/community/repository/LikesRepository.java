package com.skuteam3.fourj.community.repository;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.community.domain.GroupPost;
import com.skuteam3.fourj.community.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Boolean existsByGroupPostAndUserInfo(GroupPost groupPost, UserInfo userInfo);

    Long countByGroupPost(GroupPost groupPost);

    void deleteByGroupPostAndUserInfo(GroupPost groupPost, UserInfo userInfo);
}
