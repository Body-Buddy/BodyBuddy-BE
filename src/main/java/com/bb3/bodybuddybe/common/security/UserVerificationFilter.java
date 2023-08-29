package com.bb3.bodybuddybe.common.security;

import com.bb3.bodybuddybe.common.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserVerificationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public UserVerificationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private final Pattern pattern = Pattern.compile("^/api/users/(\\d+)(/.*)?");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        Matcher matcher = pattern.matcher(uri);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && matcher.matches()) {
            String pathUserId = matcher.group(1);
            String additionalPath = matcher.group(2);

            if ("GET".equalsIgnoreCase(request.getMethod()) && "/profile".equals(additionalPath)) {
                filterChain.doFilter(request, response);
                return;
            }

            Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getId();

            if (!userId.toString().equals(pathUserId)) {
                ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN.toString(), "로그인 유저 본인만 해당 리소스에 접근할 수 있습니다.");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

