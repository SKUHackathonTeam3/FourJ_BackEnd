package com.skuteam3.fourj.community.service;

import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.community.domain.GroupPost;
import com.skuteam3.fourj.community.domain.Likes;
import com.skuteam3.fourj.community.repository.GroupPostRepository;
import com.skuteam3.fourj.community.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikesService {

    private final LikesRepository likesRepository;
    private final GroupPostRepository groupPostRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean setLike(String userEmail, Long groupPostId){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("user not found"));
        UserInfo userInfo = user.getUserInfo();

        GroupPost groupPost = groupPostRepository.findById(groupPostId).get();
        if(likesRepository.existsByGroupPostAndUserInfo(groupPost, userInfo)){
            likesRepository.deleteByGroupPostAndUserInfo(groupPost, userInfo);
        }else{
            Likes likes = new Likes();
            likes.setGroupPost(groupPost);
            likes.setUserInfo(userInfo);
            likesRepository.save(likes);
            return true;
        }
        return false;
    }
}
