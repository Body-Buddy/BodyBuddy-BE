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
        boolean isNewUser = user.getBirthDate() == null;

        jwtUtil.handleTokenResponseForSocialLogin(user, response);

        String redirectUrl = isNewUser ? frontUrl + "/signup/social" : frontUrl + "/friends";

        response.sendRedirect(redirectUrl);
    }
}