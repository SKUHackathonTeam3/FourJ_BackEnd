package com.skuteam3.fourj.community.controller;

import com.skuteam3.fourj.community.domain.GroupPost;
import com.skuteam3.fourj.community.dto.GroupPostRequestDto;
import com.skuteam3.fourj.community.dto.GroupPostResponseDto;
import com.skuteam3.fourj.community.service.GroupPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name = "groupPosts", description = "단체 게시판 게시글 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/group-posts")
public class GroupPostController {

    private final GroupPostService groupPostService;

    //Create
    @Operation(
            summary = "단체 게시판 게시글 생성",
            description = "유저인포에 따른 게시글을 생성합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 게시글을 생성합니다.(유저의 ABTI를 닉네임으로 사용하므로, ABTI를 설정한 후 사용할 수 있습니다.)"
    )
    @PostMapping
    public String createGroupPost(@RequestBody GroupPostRequestDto groupPostDto, Authentication authentication){
        String userEmail = authentication.getName();

        if (groupPostService.createGroupPost(groupPostDto, userEmail) == null) {
            return "GroupPost created failed: User dose not set the Abti";
        }
        return "GroupPost created successfully";
    }

    //Read
    @Operation(
            summary = "단체 게시판 게시글 모두 조회",
            description = "단체 게시판 게시글을 모두 조회합니다. " +
                    "유저의 ABTI가 설정되어 있지 않은 경우 에러가 발생합니다."
    )
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

    @Operation(
            summary = "단체 게시판 게시글 ID로 조회",
            description = "단체 게시판 게시글을 게시글 ID로 조회합니다. "+
                    "유저의 ABTI가 설정되어 있지 않은 경우 에러가 발생합니다.",
            parameters = {
                    @Parameter(name = "groupPostId", description = "[path_variable] groupPost의 Id", required = true)
            }
    )
    @GetMapping("/{groupPostId}")
    public ResponseEntity<GroupPostResponseDto> getGroupPostById(@PathVariable Long groupPostId){
        Optional<GroupPost> postOptional = groupPostService.getGroupPostById(groupPostId);
        if (postOptional.isPresent()){
            GroupPostResponseDto dto = GroupPostResponseDto.from(postOptional.get(), groupPostService.getLikesByGroupPostId(postOptional.get().getId()));
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "단체 게시판 게시글 해시태그로 조회",
            description = "단체 게시판 게시글을 게시글의 해시태그로 조회합니다. "+
                    "유저의 ABTI가 설정되어 있지 않은 경우 에러가 발생합니다.",
            parameters = {
                    @Parameter(name = "hashtag", description = "[path_variable] hashtag", required = true)
            }
    )
    @GetMapping("/hashtag/{hashtag}")
    public ResponseEntity<List<GroupPostResponseDto>> getGroupPostByHashtag(@PathVariable String hashtag) {
        List<GroupPost> groupPosts = groupPostService.getGroupPostByHashtag(hashtag);
        List<GroupPostResponseDto> groupPostDtos = new ArrayList<>();
        for (GroupPost groupPost : groupPosts) {
            GroupPostResponseDto dto = GroupPostResponseDto.from(groupPost, groupPostService.getLikesByGroupPostId(groupPost.getId()));
            groupPostDtos.add(dto);
        }
        return ResponseEntity.ok(groupPostDtos);
    }

    @Operation(
            summary = "단체 게시판 게시글 키워드로 조회",
            description = "단체 게시판 게시글을 게시글의 제목 또는 본문에 포함된 키워드로 조회합니다. "+
                    "유저의 ABTI가 설정되어 있지 않은 경우 에러가 발생합니다.",
            parameters = {
                    @Parameter(name = "keyword", description = "[path_variable] keyword", required = true)
            }
    )
    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<List<GroupPostResponseDto>> getGroupPostByKeyword(@PathVariable String keyword) {
        List<GroupPost> groupPosts = groupPostService.getGroupPostByKeyword(keyword);
        List<GroupPostResponseDto> groupPostDtos = new ArrayList<>();
        for (GroupPost groupPost : groupPosts) {
            GroupPostResponseDto dto = GroupPostResponseDto.from(groupPost, groupPostService.getLikesByGroupPostId(groupPost.getId()));
            groupPostDtos.add(dto);
        }
        return ResponseEntity.ok(groupPostDtos);
    }

    @Operation(
            summary = "단체 게시판 흑역사 베스트 게시글 조회",
            description = "단체 게시판에서 흑역사 베스트 게시글을 조회합니다. " +
                    "해당 게시글은 흑역사 키워드 포함, 좋아요 10개 이상의 상위 5개 게시글이 해당됩니다. "+
                    "유저의 ABTI가 설정되어 있지 않은 경우 에러가 발생합니다.",
            parameters = {
                    @Parameter(name = "hashtag", description = "[path_variable] hashtag", required = true)
            }
    )
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
    @Operation(
            summary = "단체 게시판 게시글 수정",
            description = "유저인포에 따른 단체 게시판 게시글을 수정합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포의 게시글이 맞는 경우 게시글 ID로 게시글을 수정합니다.",
            parameters = {
                    @Parameter(name = "groupPostId", description = "[path_variable] groupPost의 Id", required = true)
            }
    )
    @PatchMapping("/{groupPostId}")
    public ResponseEntity<GroupPostRequestDto> updateGroupPost(@PathVariable Long groupPostId, @RequestBody GroupPostRequestDto groupPostDto) {
        try {
            GroupPost updateGroupPost = groupPostService.updateGroupPost(groupPostId, groupPostDto);
            return ResponseEntity.ok(groupPostDto.from(updateGroupPost));
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @Operation(
            summary = "단체 게시판 게시글 삭제",
            description = "유저인포에 따른 단체 게시판 게시글을 삭제합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포의 게시글이 맞는 경우 게시글을 ID로 게시글을 삭제합니다.",
            parameters = {
                    @Parameter(name = "groupPostId", description = "[path_variable] groupPost의 Id", required = true)
            }
    )
    @DeleteMapping("/{groupPostId}")
    public ResponseEntity<Void> deleteGroupPost(@PathVariable Long groupPostId){
        groupPostService.deleteGroupPost(groupPostId);
        return ResponseEntity.noContent().build();
    }

}
