package com.skuteam3.fourj.community.dto;

import com.skuteam3.fourj.community.domain.GroupPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GroupPostResponseDto {
    @Schema(description = "단체 게시판 게시글 ID", example = "1")
    private Long id;
    @Schema(description = "단체 게시판 게시글 제목", example = "확실히 혼자 마실 때 더 많이 마시게 되는 것 같네")
    private String title;
    @Schema(description = "단체 게시판 게시글 본문", example = "절주챌린지 중인 분들도 많을텐데 혼자 마시면서")
    private String contents;
    @Schema(description = "단체 게시판 게시글 해시태그", example = "#절주챌린지")
    private String hashtag;
    @Schema(description = "단체 게시판 게시글 작성날짜 자동생성", example = "2024-07-28T13:21:58.088Z")
    private LocalDateTime PostDate;
    @Schema(description = "단체 게시판 게시글 작성유저", example = "주량마스터")
    private String userName;
    @Schema(description = "단체 게시판 게시글 좋아요 수", example = "1")
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
