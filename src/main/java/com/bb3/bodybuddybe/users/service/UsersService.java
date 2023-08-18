package com.bb3.bodybuddybe.users.service;

import com.bb3.bodybuddybe.common.advice.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.users.dto.*;
import com.bb3.bodybuddybe.users.entity.Users;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UsersService {
    void signup(AuthRequestDto requestDto);

    @Transactional
    ResponseEntity<ApiResponseDto> changeUserInfo(MultipartFile profilePic, String introduction, String password, Users user) throws IOException;


    boolean isValidString(String input);

    @Transactional(readOnly = true)
    UserProfileDto getUserProfile(Users user);

    @Transactional(readOnly = true)
    ResponseEntity<ProfileResponseDto> getProfile(Users user);

    void delete(DeleteRequestDto requestDto, Users user);

}
