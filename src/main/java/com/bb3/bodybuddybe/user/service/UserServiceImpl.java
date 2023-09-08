package com.bb3.bodybuddybe.user.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.common.image.ImageUploader;
import com.bb3.bodybuddybe.common.jwt.JwtUtil;
import com.bb3.bodybuddybe.common.oauth2.entity.BlacklistedToken;
import com.bb3.bodybuddybe.common.oauth2.entity.RefreshToken;
import com.bb3.bodybuddybe.common.oauth2.repository.BlacklistedTokenRepository;
import com.bb3.bodybuddybe.common.oauth2.repository.RefreshTokenRepository;
import com.bb3.bodybuddybe.matching.enums.GenderEnum;
import com.bb3.bodybuddybe.user.dto.*;
import com.bb3.bodybuddybe.user.entity.User;
import com.bb3.bodybuddybe.user.enums.UserRoleEnum;
import com.bb3.bodybuddybe.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
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

    @Override
    public void reissueToken(ReissueRequestDto requestDto, HttpServletResponse response) {
        String refreshToken = requestDto.getRefreshToken();
        RefreshToken refreshTokenEntity = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        User user = findUserById(refreshTokenEntity.getUserId());

        String newAccessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole());
        String newRefreshToken = jwtUtil.createAndSaveRefreshToken(user.getId());

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
        response.addCookie(jwtUtil.createRefreshTokenCookie(newRefreshToken));

        refreshTokenRepository.delete(refreshTokenEntity);
        refreshTokenRepository.save(new RefreshToken(newRefreshToken, user.getId()));
    }

    @Override
    public void logout(LogoutRequestDto requestDto, HttpServletRequest request) {
        String accessToken = jwtUtil.extractTokenFromRequest(request);
        String refreshToken = requestDto.getRefreshToken();

        long expirationTime = jwtUtil.getRemainingTime(accessToken);
        blacklistedTokenRepository.save(new BlacklistedToken(accessToken, expirationTime / 1000));

        RefreshToken refreshTokenEntity = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        refreshTokenRepository.delete(refreshTokenEntity);
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
        User user = findUserById(userId);
        return new ProfileResponseDto(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}


