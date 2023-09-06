package com.bb3.bodybuddybe.user.service;

import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.user.dto.*;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService {
    @Transactional
    void signup(SignupRequestDto requestDto);

    @Transactional
    void deleteUser(UserDeleteRequestDto requestDto, User user);

    @Transactional
    void uploadProfileImage(MultipartFile file, User user);

    @Transactional(readOnly = true)
    String getProfileImage(User user);

    @Transactional
    void deleteProfileImage(User user);

    @Transactional
    void updateProfile(ProfileUpdateRequestDto requestDto, User user);

    @Transactional(readOnly = true)
    ProfileResponseDto getProfile(Long userId);

    @Transactional
    void socialAddProfile(SocialUpdateInform requestDto, User user);


}
