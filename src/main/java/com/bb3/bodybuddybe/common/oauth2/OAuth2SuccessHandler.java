package com.bb3.bodybuddybe.common.oauth2;

import com.bb3.bodybuddybe.common.jwt.JwtUtil;
import com.bb3.bodybuddybe.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    @Value("${front.server.url}")
    private String frontUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Map<String, Object> attributes = ((OAuth2User) authentication.getPrincipal()).getAttributes();
        User user = (User) attributes.get("storedUser");

        jwtUtil.handleTokenResponseForSocialLogin(user, response);

        String redirectPath = getRedirectPath(user);

        response.sendRedirect(frontUrl + redirectPath);
    }

    private String getRedirectPath(User user) {
        if (!user.getHasFinishedSocialSignup()) {
            return "/signup/social";
        } else if (!user.getHasRegisteredGym()) {
            return "/gyms/setup";
        } else if (!user.getHasSetProfile()) {
            return "/profile/setup";
        } else if (!user.getHasSetMatchingCriteria()) {
            return "/matching/setup";
        }
        return "/friends";
    }
}