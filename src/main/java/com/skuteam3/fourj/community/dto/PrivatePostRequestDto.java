package com.skuteam3.fourj.community.dto;

import com.skuteam3.fourj.community.domain.PrivatePost;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrivatePostRequestDto {
    private String contents;


    public static PrivatePostResponseDto from(PrivatePost privatePost){
        PrivatePostResponseDto privatePostDto = new PrivatePostResponseDto();
        privatePostDto.setContents(privatePost.getContents());
        return privatePostDto;
    }
}
