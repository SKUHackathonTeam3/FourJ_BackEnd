package com.skuteam3.fourj.community.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.skuteam3.fourj.community.domain.PrivatePost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrivatePostResponseDto {
    @Schema(description = "혼잣말 게시판 게시글 ID", example = "1")
    private Long id;
    @Schema(description = "혼잣말 게시판 게시글 본문", example = "크러시 맥주랑 잘 맞는 듯")
    private String contents;
    @Schema(description = "혼잣말 게시판 게시글 작성날짜 자동생성", example = "2024-07-28T13:21:58.088Z")
    private LocalDateTime postDate;

    public static PrivatePostResponseDto from(PrivatePost privatePost){
        PrivatePostResponseDto privatePostDto = new PrivatePostResponseDto();
        privatePostDto.setId(privatePost.getId());
        privatePostDto.setPostDate(privatePost.getPostDate());
        privatePostDto.setContents(privatePost.getContents());
        return privatePostDto;
    }
}
