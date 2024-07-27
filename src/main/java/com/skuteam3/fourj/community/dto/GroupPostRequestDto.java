package com.skuteam3.fourj.community.dto;

import com.skuteam3.fourj.community.domain.GroupPost;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GroupPostRequestDto {
    private String title;
    private String contents;
    private String hashtag;

    public static GroupPostRequestDto from(GroupPost groupPost){
        GroupPostRequestDto groupPostDto = new GroupPostRequestDto();

        groupPostDto.setTitle(groupPost.getTitle());
        groupPostDto.setContents(groupPost.getContents());
        groupPostDto.setHashtag(groupPost.getHashtag());

        return groupPostDto;
    }

}
