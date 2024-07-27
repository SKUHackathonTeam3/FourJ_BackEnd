package com.skuteam3.fourj.community.service;

import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.community.domain.PrivatePost;
import com.skuteam3.fourj.community.dto.PrivatePostRequestDto;
import com.skuteam3.fourj.community.dto.PrivatePostResponseDto;
import com.skuteam3.fourj.community.repository.PrivatePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PrivatePostService {

    private final PrivatePostRepository privatePostRepository;
    private final UserRepository userRepository;

    @Transactional
    public PrivatePost createPrivatePost(PrivatePostRequestDto privatePostRequestDto, String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("user not found"));
        UserInfo userInfo = user.getUserInfo();

        PrivatePost privatePost = new PrivatePost();

        privatePost.setContents(privatePostRequestDto.getContents());
        privatePost.setUserInfo(userInfo);

        return privatePostRepository.save(privatePost);
    }

    @Transactional
    public void deletePrivatePost(Long id){
        privatePostRepository.deleteById(id);
    }


    // 모든 게시글 조회
    public List<PrivatePost> getAllPrivatePosts() {
        return privatePostRepository.findAll();
    }

    // 키워드로 게시글 조회
    public List<PrivatePost> getPrivatePostByKeyword(String keyword) {
        return privatePostRepository.findByContentsContaining(keyword);
    }

}
