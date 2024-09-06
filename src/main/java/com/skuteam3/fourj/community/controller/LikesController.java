package com.skuteam3.fourj.community.controller;

import com.skuteam3.fourj.community.service.LikesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "likes", description = "단체 게시판 게시글 좋아요 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/likes")
public class LikesController {

    private final LikesService likesService;

    @Operation(
            summary = "좋아요 생성 및 삭제",
            description = "유저인포에 따른 좋아요를 생성 및 삭제합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저가 게시글에 좋아요를 누를 경우 생성, 좋아요가 이미 있는 경우 삭제합니다.",
            parameters = {
                    @Parameter(name = "groupPostid", description = "[path_variable] groupPost의 Id", required = true)
            }
    )
    @PostMapping("/{groupPostid}")
    public ResponseEntity<?> setLikeByGroupPostAndUserInfo(@PathVariable Long groupPostid, Authentication authentication){
        String userEmail = authentication.getName();

        if(likesService.setLike(userEmail, groupPostid)){
            return ResponseEntity.ok("success to create like");
        }
        return ResponseEntity.noContent().build();
    }

}
