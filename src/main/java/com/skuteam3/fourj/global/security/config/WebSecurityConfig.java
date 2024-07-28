package com.skuteam3.fourj.global.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skuteam3.fourj.account.filter.JsonUsernamePasswordAuthenticationFilter;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.account.service.UserDetailService;
import com.skuteam3.fourj.global.security.handler.LoginSuccessHandler;
import com.skuteam3.fourj.jwt.filter.JwtAuthenticationFilter;
import com.skuteam3.fourj.jwt.provider.JwtProvider;
import com.skuteam3.fourj.oauth2.service.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final CustomOauth2UserService oAuth2UserService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final UserDetailService userDetailService;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;


    @Bean
    protected SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ))
                .authorizeHttpRequests(request ->
                        request
                            /*
                                .requestMatchers(
                                "/",
                                "/api/v1/*"
                                )
                                .permitAll()
                                .requestMatchers("/api/v1/user/*")
                                .hasRole("USER")
                                .requestMatchers("/api/v1/admin/*")
                                .hasRole("ADMIN")
                            */
                                .anyRequest()
                                .permitAll()
                ).oauth2Login(oauth2 ->
                        oauth2.authorizationEndpoint(endpoint ->
                                        endpoint.baseUri(
                                                "/api/v1/oauth2/authorization"
                                        ))
                                .redirectionEndpoint(endpoint ->
                                        endpoint.baseUri(
                                                "/api/v1/oauth2/callback/**"
                                        ))
                                .userInfoEndpoint(endpoint ->
                                        endpoint.userService(
                                                oAuth2UserService
                                        ))
                                .successHandler(loginSuccessHandler)
                )
                .addFilterAfter(jsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), JsonUsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(
                List.of(
                        "http://localhost:8000",
                        "http://localhost:8080"
                )
        );
        corsConfiguration.setAllowedHeaders(
                List.of("*")
        );
        corsConfiguration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH")
        );
        corsConfiguration.setExposedHeaders(
                List.of("*")
        );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(userDetailService);

        return new ProviderManager(provider);
    }

    @Bean JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(
                jwtProvider,
                userRepository,
                userDetailService
        );
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter() {
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter = new JsonUsernamePasswordAuthenticationFilter(
                authenticationManager(),
                objectMapper
        );

        jsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);

        return jsonUsernamePasswordAuthenticationFilter;
    }

}
