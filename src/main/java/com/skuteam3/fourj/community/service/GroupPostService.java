package com.skuteam3.fourj.community.service;

import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.community.dto.GroupPostRequestDto;
import com.skuteam3.fourj.community.domain.GroupPost;
import com.skuteam3.fourj.community.dto.GroupPostResponseDto;
import com.skuteam3.fourj.community.repository.GroupPostRepository;
import com.skuteam3.fourj.community.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GroupPostService {
    private final GroupPostRepository groupPostRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public GroupPostResponseDto createGroupPost(GroupPostRequestDto groupPostDto, String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("user not found"));
        UserInfo userInfo = user.getUserInfo();

        if (userInfo.getAbti() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GroupPost created failed: User dose not set the Abti");
        }

        GroupPost groupPost = new GroupPost();

        groupPost.setTitle(groupPostDto.getTitle());
        groupPost.setContents(groupPostDto.getContents());
        groupPost.setHashtag(groupPostDto.getHashtag());
        groupPost.setUserInfo(userInfo);

        return GroupPostResponseDto.from(groupPostRepository.save(groupPost), 0);
    }

    @Transactional
    public GroupPost updateGroupPost(Long id, GroupPostRequestDto groupPostDto){
        Optional<GroupPost> groupPostOptional = groupPostRepository.findById(id);
        if(groupPostOptional.isPresent()){
            GroupPost groupPost = groupPostOptional.get();
            if(groupPostDto.getTitle() != null){
                groupPostDto.setTitle(groupPostDto.getTitle());
            }
            if(groupPostDto.getContents() != null){
                groupPostDto.setContents(groupPostDto.getContents());
            }
            if(groupPostDto.getHashtag() != null){
                groupPostDto.setHashtag(groupPostDto.getHashtag());
            }
            return groupPostRepository.save(groupPost);
        }else {
            throw new RuntimeException("GroupCommunity not found with id" + id);
        }
    }

    @Transactional
    public void deleteGroupPost(Long id){
        groupPostRepository.deleteById(id);
    }

    // 모든 게시글 조회
    public List<GroupPost> getAllGroupPosts() {
        return groupPostRepository.findAll();
    }

    // Id로 게시글 조회
    public Optional<GroupPost> getGroupPostById(Long id){
        return groupPostRepository.findById(id);
    }

    // 해시태그로 게시글 조회
    public List<GroupPost> getGroupPostByHashtag(String hashtag){
        return groupPostRepository.findGroupPostByHashtagContaining(hashtag);
    }

    // 키워드로 제목,본문 게시글 조회
    public List<GroupPost> getGroupPostByKeyword(String keyword) {
        return groupPostRepository.findByTitleContainingOrContentsContaining(keyword, keyword);
    }

    // 오늘의 흑역사 베스트 게시글 조회
    // #흑역사 해시태그 포함, 좋아요 10개 이상의 상위 5개 게시글
    public List<GroupPost> getBestTop5ByLikes(String hashtag){
        return groupPostRepository.findTop5GroupPostsWithLikesGreaterThanNine(hashtag);
    }

    public Integer getLikesByGroupPostId(Long id){
        return likesRepository.countByGroupPost(groupPostRepository.findById(id).get()).intValue();
    }

}
