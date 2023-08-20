package com.bb3.bodybuddybe.common.security;

import com.bb3.bodybuddybe.common.advice.ApiResponseDto;
import com.bb3.bodybuddybe.common.jwt.JwtUtil;
import com.bb3.bodybuddybe.users.UsersRoleEnum;
import com.bb3.bodybuddybe.users.dto.AuthRequestDto;
import com.bb3.bodybuddybe.users.entity.Users;
import com.bb3.bodybuddybe.users.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static com.bb3.bodybuddybe.users.UsersBlockEnum.차단;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UsersRepository usersRepository) {
        this.jwtUtil = jwtUtil;
        this.usersRepository = usersRepository;
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            AuthRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), AuthRequestDto.class);
            Users user = usersRepository.findByUsername(requestDto.getUsername()).orElseThrow(()-> new IllegalArgumentException("유저가 없습니다."));
            if (user.getStatus().equals(차단)) {
                ApiResponseDto apiResponseDto = new ApiResponseDto("차단된 회원입니다.", HttpStatus.BAD_REQUEST.value());
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String json = new ObjectMapper().writeValueAsString(apiResponseDto);
                response.getWriter().write(json);
                return null;
            }
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인성공");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UsersRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setMsg("로그인 성공");
        apiResponseDto.setStatusCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = new ObjectMapper().writeValueAsString(apiResponseDto);
        response.getWriter().write(json);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
