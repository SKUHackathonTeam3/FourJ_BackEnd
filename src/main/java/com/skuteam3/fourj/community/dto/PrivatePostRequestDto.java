package com.skuteam3.fourj.community.dto;

import com.skuteam3.fourj.community.domain.PrivatePost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrivatePostRequestDto {
    @Schema(description = "혼잣말 게시판 게시글 본문", example = "크러시 맥주랑 잘 맞는 듯")
    private String contents;


    public static PrivatePostResponseDto from(PrivatePost privatePost){
        PrivatePostResponseDto privatePostDto = new PrivatePostResponseDto();
        privatePostDto.setContents(privatePost.getContents());
        return privatePostDto;
    }
}
