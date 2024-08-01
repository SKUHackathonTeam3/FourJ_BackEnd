package com.skuteam3.fourj.jwt.provider;

import com.skuteam3.fourj.jwt.TokenType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration.access}")
    private long JWT_EXPIRATION_ACCESS_TOKEN;

    @Value("${jwt.expiration.refresh}")
    private long JWT_EXPIRATION_REFRESH_TOKEN;

    public String createToken(String userEmail, TokenType type) {

        Date now = new Date();
        Date expiredDate = null;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        if (type == TokenType.ACCESS_TOKEN) { expiredDate = new Date(now.getTime() + JWT_EXPIRATION_ACCESS_TOKEN); }
        else if (type == TokenType.REFRESH_TOKEN) { expiredDate = new Date(now.getTime() + JWT_EXPIRATION_REFRESH_TOKEN); }

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(userEmail)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .compact();
    }

    public String reIssueAccessTokenFromRefreshToken(String refreshToken) {

        String userEmail = validate(refreshToken);

        return createToken(userEmail, TokenType.ACCESS_TOKEN);
    }

    public String validate(String jwt) {

        String subject;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {

            subject = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {

            log.info("JwtProvider_validate", e);

            return null;
        }

        return subject;
    }

    public String parseToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {

            return bearerToken.substring(7);
        }

        return null;
    }
}
