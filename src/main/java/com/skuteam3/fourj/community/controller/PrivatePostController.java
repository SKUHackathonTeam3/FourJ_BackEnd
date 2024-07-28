package com.skuteam3.fourj.community.controller;

import com.skuteam3.fourj.community.domain.PrivatePost;
import com.skuteam3.fourj.community.dto.PrivatePostRequestDto;
import com.skuteam3.fourj.community.dto.PrivatePostResponseDto;
import com.skuteam3.fourj.community.service.PrivatePostService;
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

@Tag(name = "privatePosts", description = "혼잣말 게시판 게시글 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/private-posts")
public class PrivatePostController{

    private final PrivatePostService privatePostService;

    //Create
    @Operation(
            summary = "혼잣말 게시판 게시글 생성",
            description = "유저인포에 따른 게시글을 생성합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 게시글을 생성합니다."
    )
    @PostMapping
    public String createPrivatePost(@RequestBody PrivatePostRequestDto privatePostRequestDto, Authentication authentication){
        String userEmail = authentication.getName();

        privatePostService.createPrivatePost(privatePostRequestDto, userEmail);
        return "PrivatePost created successfully";
    }

    //Read
    @Operation(
            summary = "혼잣말 게시판 게시글 모두 조회",
            description = "혼잣말 게시판 게시글을 모두 조회합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포의 게시글이 맞는 경우 게시글을 모두 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<PrivatePostResponseDto>> getAllPrivatePosts(Authentication authentication) {
        String userEmail = authentication.getName();

        List<PrivatePost> privatePosts = privatePostService.getAllPrivatePosts();
        List<PrivatePostResponseDto> privatePostDtos = new ArrayList<>();
        for (PrivatePost privatePost : privatePosts) {
            PrivatePostResponseDto dto = PrivatePostResponseDto.from(privatePost);
            privatePostDtos.add(dto);
        }
        return ResponseEntity.ok(privatePostDtos);
    }

    @Operation(
            summary = "혼잣말 게시판 게시글 ID로 조회",
            description = "혼잣말 게시판 게시글을 게시글 ID로 조회합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포의 게시글이 맞는 경우 게시글을 게시글 ID로 조회합니다.",
            parameters = {
                    @Parameter(name = "keyword", description = "[path_variable] keyword", required = true)
            }
    )
    @GetMapping("/{privatePostId}")
    public ResponseEntity<PrivatePostResponseDto> getPrivatePostById(@PathVariable Long id){
        Optional<PrivatePost> postOptional = privatePostService.getPrivatePostById(id);
        if (postOptional.isPresent()){
            PrivatePostResponseDto dto = PrivatePostResponseDto.from(postOptional.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(
            summary = "혼잣말 게시판 게시글 키워드로 조회",
            description = "혼잣말 게시판 게시글을 게시글의 본문에 포함된 키워드로 조회합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포의 게시글이 맞는 경우 키워드로 게시글을 조회합니다.",
            parameters = {
                    @Parameter(name = "keyword", description = "[path_variable] privatePost의 keyword", required = true)
            }
    )
    @GetMapping("/{keyword}")
    public ResponseEntity<List<PrivatePostResponseDto>> getPrivatePostByKeyword(@PathVariable String keyword, Authentication authentication) {
        String userEmail = authentication.getName();

        List<PrivatePost> privatePosts = privatePostService.getPrivatePostByKeyword(keyword);
        List<PrivatePostResponseDto> privatePostDtos = new ArrayList<>();
        for (PrivatePost privatePost : privatePosts) {
            PrivatePostResponseDto dto = PrivatePostResponseDto.from(privatePost);
            privatePostDtos.add(dto);
        }
        return ResponseEntity.ok(privatePostDtos);
    }

    //Delete
    @Operation(
            summary = "혼잣말 게시판 게시글 삭제",
            description = "유저인포에 따른 혼잣말 게시판 게시글을 삭제합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포의 게시글이 맞는 경우 게시글을 ID로 게시글을 삭제합니다.",
            parameters = {
                    @Parameter(name = "privatePostId", description = "[path_variable] privatePost의 Id", required = true)
            }
    )
    @DeleteMapping("/{privatePostId}")
    public ResponseEntity<Void> deletePrivatePost(@PathVariable Long id){
        privatePostService.deletePrivatePost(id);
        return ResponseEntity.noContent().build();
    }

}
