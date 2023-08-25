package com.bb3.bodybuddybe.user.service;

import com.bb3.bodybuddybe.user.dto.*;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserService {
    @Transactional
    void signup(SignupRequestDto requestDto);

    @Transactional
    void changeStatus(UserStatusRequestDto requestDto, User user);

    @Transactional
    void uploadProfileImage(MultipartFile file, User user) throws IOException;

    @Transactional
    void updateProfile(ProfileUpdateRequestDto requestDto, User user);

    @Transactional(readOnly = true)
    ProfileResponseDto getProfile(Long userId);
}
