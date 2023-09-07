package com.bb3.bodybuddybe.user.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.common.image.ImageUploader;
import com.bb3.bodybuddybe.common.jwt.JwtUtil;
import com.bb3.bodybuddybe.common.oauth2.entity.LogoutList;
import com.bb3.bodybuddybe.common.oauth2.entity.RefreshToken;
import com.bb3.bodybuddybe.common.oauth2.repository.LogoutlistRepository;
import com.bb3.bodybuddybe.common.oauth2.repository.RefreshTokenRepository;
import com.bb3.bodybuddybe.matching.enums.GenderEnum;
import com.bb3.bodybuddybe.user.dto.*;
import com.bb3.bodybuddybe.user.entity.User;
import com.bb3.bodybuddybe.user.enums.UserRoleEnum;
import com.bb3.bodybuddybe.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Key;
import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogoutlistRepository logoutlistRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final ImageUploader imageUploader;

    @Override
    @Transactional
    public void signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());
        GenderEnum gender = requestDto.getGender();
        LocalDate birthDate = requestDto.getBirthDate();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        User user = new User(email, password, gender, birthDate, UserRoleEnum.USER);

        if (user.getAge() < 14) {
            throw new CustomException(ErrorCode.UNDER_AGE);
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void socialSignup(SocialSignupRequestDto requestDto, User user) {
        user.socialSignup(requestDto);
        userRepository.save(user);
    }

    public void logout(User user, HttpServletRequest request) {
        String refreshTokenVal = request.getHeader("RefreshToken").substring(7);
        String accessTokenVal = jwtUtil.getJwtFromHeader(request);

        try {
            Key key = jwtUtil.getKey();
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessTokenVal)
                    .getBody()
                    .getExpiration();
            long expireTime = expiration.getTime() - System.currentTimeMillis();
            logoutlistRepository.save(new LogoutList(accessTokenVal, expireTime / 1000));// MILLS를 1000으로 나눠서 초로 바꿔줌
            RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenVal)
                    .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 없습니다."));
            refreshTokenRepository.delete(refreshToken);
            System.out.println(expiration.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void changePassword(PasswordChangeRequestDto requestDto, User user) {
        String password = requestDto.getPassword();
        user.updatePassword(password);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(UserDeleteRequestDto requestDto, User user) {
        if (!user.getPassword().equals(passwordEncoder.encode(requestDto.getPassword()))) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void uploadProfileImage(MultipartFile file, User user) {
        String imageUrl = imageUploader.upload(file);
        user.updateImageUrl(imageUrl);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public String getProfileImage(User user) {
        return user.getImageUrl();
    }

    @Override
    @Transactional
    public void deleteProfileImage(User user) {
        imageUploader.deleteFromUrl(user.getImageUrl());
        user.updateImageUrl(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateProfile(ProfileUpdateRequestDto requestDto, User user) {
        user.updateProfile(requestDto);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long userId) {

        User user = findById(userId);
        return new ProfileResponseDto(user);
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }


}


