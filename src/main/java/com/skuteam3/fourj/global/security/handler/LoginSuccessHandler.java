package com.skuteam3.fourj.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.jwt.TokenType;
import com.skuteam3.fourj.jwt.provider.JwtProvider;
import com.skuteam3.fourj.oauth2.domain.SocialUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler  implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Optional<User> userOptional = userRepository.findByEmail(authentication.getName());
        if (userOptional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
            return;
        }
        UserInfo userInfo = userOptional.get().getUserInfo();

        String accessToken = jwtProvider.createToken(authentication.getName(), TokenType.ACCESS_TOKEN);
        String refreshToken = jwtProvider.createToken(authentication.getName(), TokenType.REFRESH_TOKEN);


        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(24*60*60)
                .sameSite("None")
                .build();


        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Set-Cookie", cookie.toString());
        response.setHeader("Access-Control-Allow-Origin", "https://jujeokjujeok.netlify.app");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if (authentication instanceof OAuth2AuthenticationToken) {

            response.sendRedirect("https://jujeokjujeok.netlify.app/?socialLogin=true");
        }
        if (authentication instanceof UsernamePasswordAuthenticationToken) {

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("accessToken", accessToken);
            responseBody.put("message", userInfo.getAbti() == null ? "Need ABTI" : "Login successful");

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
            response.getWriter().flush();
        }

        System.out.println("refresh token: " + refreshToken + "\naccess token: " + accessToken);
    }
}
