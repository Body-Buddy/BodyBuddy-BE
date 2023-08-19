package com.bb3.bodybuddybe.common.security;

import com.bb3.bodybuddybe.common.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class UserVerificationFilter implements Filter {

    private final ObjectMapper objectMapper;

    public UserVerificationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String pathUserId = httpServletRequest.getParameter("userId");
        String requestURI = httpServletRequest.getRequestURI();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            Long userId = userDetails.getUser().getId();

            if (requestURI.matches("/api/users/\\d+/gyms(/\\d+)?")) {
                if (!userId.toString().equals(pathUserId)) {
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;

                    ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN.toString(), "로그인 유저 본인만 해당 리소스에 접근할 수 있습니다.");
                    httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    httpServletResponse.setContentType("application/json; charset=UTF-8");
                    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}

