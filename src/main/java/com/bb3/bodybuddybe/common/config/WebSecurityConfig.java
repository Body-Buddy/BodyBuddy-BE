package com.bb3.bodybuddybe.common.config;

import com.bb3.bodybuddybe.common.jwt.JwtUtil;
import com.bb3.bodybuddybe.common.jwt.TokenResponseHandler;
import com.bb3.bodybuddybe.common.oauth2.CustomOAuth2UserService;
import com.bb3.bodybuddybe.common.oauth2.OAuth2SuccessHandler;
import com.bb3.bodybuddybe.common.security.JwtAuthenticationFilter;
import com.bb3.bodybuddybe.common.security.JwtAuthorizationFilter;
import com.bb3.bodybuddybe.common.security.UserDetailsServiceImpl;
import com.bb3.bodybuddybe.common.security.UserVerificationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final UserDetailsServiceImpl userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final TokenResponseHandler tokenResponseHandler;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(objectMapper, tokenResponseHandler);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, objectMapper, userDetailsService);
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new OAuth2SuccessHandler(tokenResponseHandler);
    }

    @Bean
    public UserVerificationFilter userVerificationFilter() {
        return new UserVerificationFilter(objectMapper);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://bodybuddy.vercel.app"));
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.oauth2Login(oauth2Login ->
                oauth2Login
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.userService(customOAuth2UserService)
                        )
                        .successHandler(authenticationSuccessHandler())
        );

        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                        .anyRequest().permitAll()
        );

        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(userVerificationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
