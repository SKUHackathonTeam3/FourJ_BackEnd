package com.skuteam3.fourj.jwt.filter;


import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.jwt.provider.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            String token = jwtProvider.parseToken(request);

            if (token != null) {
                String userEmail = jwtProvider.validate(token);

                if (userEmail == null) {

                    filterChain.doFilter(request, response);

                    return;
                }

                User user = userRepository.findByEmail(userEmail).orElseThrow(null);
                String role = user.getUserRole();
                List<GrantedAuthority> authorities = new ArrayList<>();

                authorities.add(new SimpleGrantedAuthority(role));

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userEmail, null, authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            } else {

                filterChain.doFilter(request, response);

                return;
            }
        } catch (Exception e) {

            log.info("JwtAuthenticationFilter_doFilterInternal", e);
        }

        filterChain.doFilter(request, response);
    }



}
