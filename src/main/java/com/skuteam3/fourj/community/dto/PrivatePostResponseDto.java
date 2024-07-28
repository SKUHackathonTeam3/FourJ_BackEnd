package com.skuteam3.fourj.community.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.skuteam3.fourj.community.domain.PrivatePost;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrivatePostResponseDto {
    private Long id;
    private String contents;
    private LocalDateTime postDate;

    public static PrivatePostResponseDto from(PrivatePost privatePost){
        PrivatePostResponseDto privatePostDto = new PrivatePostResponseDto();
        privatePostDto.setId(privatePost.getId());
        privatePostDto.setPostDate(privatePost.getPostDate());
        privatePostDto.setContents(privatePost.getContents());
        return privatePostDto;
    }
}
