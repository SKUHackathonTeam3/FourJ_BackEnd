package com.skuteam3.fourj.jwt.controller;

import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.jwt.provider.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Tag(name="tokens", description="토큰 발급 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tokens")
public class TokenController {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Operation(
            summary = "토큰 재발급",
            description = "Refresh 토큰으로 Access 토큰을 재발급합니다. " +
                    "HTTP 쿠키에 적재된 Refresh 토큰을 이용하여 Access 토큰을 발급합니다. "
    )
    @GetMapping("/refresh")
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

        User refreshTokenUser = userRepository.findByEmail(jwtProvider.validate(refreshToken)).orElse(null);
        if (refreshTokenUser == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 Refresh 토큰입니다.");

        accessToken = jwtProvider.reIssueAccessTokenFromRefreshToken(refreshToken);

        User accessTokenUser = userRepository.findByEmail(jwtProvider.validate(accessToken)).orElse(null);
        if (accessTokenUser == null || accessTokenUser != refreshTokenUser) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("토큰 생성에 실패하였습니다.");

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", accessToken);

        UserInfo userInfo = accessTokenUser.getUserInfo();

        response.put("NeedAbti", userInfo.getAbti() == null);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        return ResponseEntity.ok().headers(headers).body(response);
    }
}
