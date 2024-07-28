package com.skuteam3.fourj.community.controller;

import com.skuteam3.fourj.community.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/likes")
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/{groupPostid}")
    public ResponseEntity<?> setLikeByGroupPostAndUserInfo(@PathVariable Long groupPostid, Authentication authentication){
        String userEmail = authentication.getName();

        if(likesService.setLike(userEmail, groupPostid)){
            return ResponseEntity.ok("success to create like");
        }
        return ResponseEntity.noContent().build();
    }

}
