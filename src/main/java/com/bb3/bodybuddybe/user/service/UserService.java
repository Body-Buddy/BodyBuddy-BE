package com.bb3.bodybuddybe.user.service;


import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.user.dto.*;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserService {
    void signup(AuthRequestDto requestDto);

    @Transactional
    ResponseEntity<ApiResponseDto> changeUserInfo(MultipartFile profilePic, String introduction, String password, User user) throws IOException;


    boolean isValidString(String input);

    @Transactional(readOnly = true)
    UserProfileDto getUserProfile(User user);

    @Transactional(readOnly = true)
    ResponseEntity<ProfileResponseDto> getProfile(User user);

    void delete(DeleteRequestDto requestDto, User user);

}
