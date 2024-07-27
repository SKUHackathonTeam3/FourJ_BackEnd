package com.skuteam3.fourj.community.controller;

import com.skuteam3.fourj.community.domain.PrivatePost;
import com.skuteam3.fourj.community.dto.PrivatePostRequestDto;
import com.skuteam3.fourj.community.dto.PrivatePostResponseDto;
import com.skuteam3.fourj.community.service.PrivatePostService;
import com.skuteam3.fourj.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/privatePost")
public class PrivatePostController{

    private final JwtProvider jwtProvider;
    private final PrivatePostService privatePostService;

    //Create
    @PostMapping
    public String createPrivatePost(HttpServletRequest request, @RequestBody PrivatePostRequestDto privatePostRequestDto){
        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        privatePostService.createPrivatePost(privatePostRequestDto, userEmail);
        return "PrivatePost created successfully";
    }

    //Read
    @GetMapping
    public ResponseEntity<List<PrivatePostResponseDto>> getAllPrivatePosts(HttpServletRequest request) {
        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        List<PrivatePost> privatePosts = privatePostService.getAllPrivatePosts();
        List<PrivatePostResponseDto> privatePostDtos = new ArrayList<>();
        for (PrivatePost privatePost : privatePosts) {
            PrivatePostResponseDto dto = PrivatePostResponseDto.from(privatePost);
            privatePostDtos.add(dto);
        }
        return ResponseEntity.ok(privatePostDtos);
    }

    @GetMapping("/{keyword}")
    public ResponseEntity<List<PrivatePostResponseDto>> getPrivatePostByKeyword(HttpServletRequest request, @PathVariable String keyword) {
        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        List<PrivatePost> privatePosts = privatePostService.getPrivatePostByKeyword(keyword);
        List<PrivatePostResponseDto> privatePostDtos = new ArrayList<>();
        for (PrivatePost privatePost : privatePosts) {
            PrivatePostResponseDto dto = PrivatePostResponseDto.from(privatePost);
            privatePostDtos.add(dto);
        }
        return ResponseEntity.ok(privatePostDtos);
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrivatePost(@PathVariable Long id){
        privatePostService.deletePrivatePost(id);
        return ResponseEntity.noContent().build();
    }

}
