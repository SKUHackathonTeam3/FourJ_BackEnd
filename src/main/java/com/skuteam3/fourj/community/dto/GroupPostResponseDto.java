package com.skuteam3.fourj.community.dto;

import com.skuteam3.fourj.community.domain.GroupPost;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GroupPostResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String hashtag;
    private LocalDateTime PostDate;
    private String userName;
    private Integer likes;

    public static GroupPostResponseDto from(GroupPost groupPost,Integer likes){
        GroupPostResponseDto groupPostDto = new GroupPostResponseDto();

        groupPostDto.setId(groupPost.getId());
        groupPostDto.setTitle(groupPost.getTitle());
        groupPostDto.setContents(groupPost.getContents());
        groupPostDto.setHashtag(groupPost.getHashtag());
        groupPostDto.setPostDate(groupPost.getPostDate());
        groupPostDto.setUserName(groupPost.getUserInfo().getAbti().getTitle());
        groupPostDto.setLikes(likes);

        return groupPostDto;
    }
}
