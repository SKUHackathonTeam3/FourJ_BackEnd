package com.skuteam3.fourj.jwt.controller;

import com.skuteam3.fourj.jwt.provider.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="tokens", description="토큰 발급 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tokens")
public class TokenController {


    private final JwtProvider jwtProvider;

    @Operation(
            summary = "토큰 재발급",
            description = "Refresh 토큰으로 Access 토큰을 재발급합니다. " +
                    "HTTP 쿠키에 적재된 Refresh 토큰을 이용하여 Access 토큰을 발급합니다. "
    )
    @PostMapping("/refresh")
    public ResponseEntity<?> reissueAccessToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String refreshToken = "";
        String accessToken = "";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        accessToken = jwtProvider.reIssueAccessTokenFromRefreshToken(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        return ResponseEntity.ok().headers(headers).build();
    }
}
