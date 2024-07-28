package com.skuteam3.fourj.community.dto;

import com.skuteam3.fourj.community.domain.GroupPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GroupPostRequestDto {
    @Schema(description = "단체 게시판 게시글 제목", example = "확실히 혼자 마실 때 더 많이 마시게 되는 것 같네")
    private String title;
    @Schema(description = "단체 게시판 게시글 본문", example = "절주챌린지 중인 분들도 많을텐데 혼자 마시면서")
    private String contents;
    @Schema(description = "단체 게시판 게시글 해시태그", example = "#절주챌린지")
    private String hashtag;

    public static GroupPostRequestDto from(GroupPost groupPost){
        GroupPostRequestDto groupPostDto = new GroupPostRequestDto();

        groupPostDto.setTitle(groupPost.getTitle());
        groupPostDto.setContents(groupPost.getContents());
        groupPostDto.setHashtag(groupPost.getHashtag());

        return groupPostDto;
    }

}
