package com.bb3.bodybuddybe.common.jwt;

import com.bb3.bodybuddybe.common.oauth2.entity.RefreshToken;
import com.bb3.bodybuddybe.common.oauth2.repository.BlacklistedTokenRepository;
import com.bb3.bodybuddybe.common.oauth2.repository.RefreshTokenRepository;
import com.bb3.bodybuddybe.user.entity.User;
import com.bb3.bodybuddybe.user.enums.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
@Getter
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L * 3; // 3시간
    private static final long REFRESH_TOKEN_TIME = 60 * 60 * 1000L * 24 * 3; // 3일

    @Value("${jwt.secretKey}")
    private String secretKey;

    private Key key;

    private final RefreshTokenRepository refreshTokenRepository;

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void handleTokenResponse(User user, HttpServletResponse response) {
        String accessToken = createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = URLEncoder.encode(createRefreshToken(), StandardCharsets.UTF_8);

        response.addHeader(AUTHORIZATION_HEADER, accessToken);
        setRefreshTokenInCookie(refreshToken, response);

        refreshTokenRepository.save(new RefreshToken(refreshToken, user.getId()));
    }

    public void handleTokenResponseForSocialLogin(User user, HttpServletResponse response) {
        String accessToken = URLEncoder.encode(createAccessToken(user.getEmail(), user.getRole()), StandardCharsets.UTF_8);
        String refreshToken = URLEncoder.encode(createRefreshToken(), StandardCharsets.UTF_8);

        setAccessTokenInCookie(accessToken, response);
        setRefreshTokenInCookie(refreshToken, response);

        refreshTokenRepository.save(new RefreshToken(refreshToken, user.getId()));
    }

    public String createAccessToken(String username, UserRoleEnum role) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_TIME))
                .setIssuedAt(now)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();

        return Jwts.builder()
                .setClaims(Jwts.claims())
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME))
                .setIssuedAt(now)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public void setAccessTokenInCookie(String accessToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setPath("/");
        cookie.setMaxAge((int) ACCESS_TOKEN_TIME / 1000);
        response.addCookie(cookie);
    }

    public void setRefreshTokenInCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge((int) REFRESH_TOKEN_TIME / 1000);
        response.addCookie(cookie);
    }

    public boolean isValidToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsById(token);
    }

    public long getRemainingTime(String token) {
        Claims claims = getClaims(token);
        Date expiration = claims.getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
