package com.skuteam3.fourj.community.controller;

import com.skuteam3.fourj.community.domain.GroupPost;
import com.skuteam3.fourj.community.dto.GroupPostRequestDto;
import com.skuteam3.fourj.community.dto.GroupPostResponseDto;
import com.skuteam3.fourj.community.service.GroupPostService;
import com.skuteam3.fourj.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/groupPost")
public class GroupPostController {

    private final JwtProvider jwtProvider;
    private final GroupPostService groupPostService;

    //Create
    @PostMapping
    public String createGroupPost(HttpServletRequest request, @RequestBody GroupPostRequestDto groupPostDto){
        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        groupPostService.createGroupPost(groupPostDto, userEmail);
        return "GroupPost created successfully";
    }

    //Read
    @GetMapping
    public ResponseEntity<List<GroupPostResponseDto>> getAllGroupPosts() {
        List<GroupPost> groupPosts = groupPostService.getAllGroupPosts();
        List<GroupPostResponseDto> groupPostDtos = new ArrayList<>();
        for (GroupPost groupPost : groupPosts) {
            GroupPostResponseDto dto = GroupPostResponseDto.from(groupPost, groupPostService.getLikesByGroupPostId(groupPost.getId()));
            groupPostDtos.add(dto);
        }
        return ResponseEntity.ok(groupPostDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupPostResponseDto> getGroupPostById(@PathVariable Long id){
        Optional<GroupPost> postOptional = groupPostService.getGroupPostById(id);
        if (postOptional.isPresent()){
            GroupPostResponseDto dto = GroupPostResponseDto.from(postOptional.get(), groupPostService.getLikesByGroupPostId(postOptional.get().getId()));
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{hashtag}")
    public ResponseEntity<List<GroupPostResponseDto>> getGroupPostByHashtag(@PathVariable String hashtag) {
        List<GroupPost> groupPosts = groupPostService.getGroupPostByHashtag(hashtag);
        List<GroupPostResponseDto> groupPostDtos = new ArrayList<>();
        for (GroupPost groupPost : groupPosts) {
            GroupPostResponseDto dto = GroupPostResponseDto.from(groupPost, groupPostService.getLikesByGroupPostId(groupPost.getId()));
            groupPostDtos.add(dto);
        }
        return ResponseEntity.ok(groupPostDtos);
    }

    @GetMapping("/{keyword}")
    public ResponseEntity<List<GroupPostResponseDto>> getGroupPostByKeyword(@PathVariable String keyword) {
        List<GroupPost> groupPosts = groupPostService.getGroupPostByKeyword(keyword);
        List<GroupPostResponseDto> groupPostDtos = new ArrayList<>();
        for (GroupPost groupPost : groupPosts) {
            GroupPostResponseDto dto = GroupPostResponseDto.from(groupPost, groupPostService.getLikesByGroupPostId(groupPost.getId()));
            groupPostDtos.add(dto);
        }
        return ResponseEntity.ok(groupPostDtos);
    }

    @GetMapping("/best/{hashtag}")
    public ResponseEntity<List<GroupPostResponseDto>> getBestTop5ByLikes(@PathVariable String hashtag) {
        List<GroupPost> groupPosts = groupPostService.getBestTop5ByLikes(hashtag);
        List<GroupPostResponseDto> groupPostDtos = new ArrayList<>();
        for (GroupPost groupPost : groupPosts) {
            GroupPostResponseDto dto = GroupPostResponseDto.from(groupPost, groupPostService.getLikesByGroupPostId(groupPost.getId()));
            groupPostDtos.add(dto);
        }
        return ResponseEntity.ok(groupPostDtos);
    }


    //Update
    @PatchMapping("/{id}")
    public ResponseEntity<GroupPostRequestDto> updateGroupPost(@PathVariable Long id, @RequestBody GroupPostRequestDto groupPostDto) {
        try {
            GroupPost updateGroupPost = groupPostService.updateGroupPost(id, groupPostDto);
            return ResponseEntity.ok(groupPostDto.from(updateGroupPost));
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupPost(@PathVariable Long id){
        groupPostService.deleteGroupPost(id);
        return ResponseEntity.noContent().build();
    }

}
