package com.bb3.bodybuddybe.user.controller;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.user.dto.*;
import com.bb3.bodybuddybe.user.service.EmailServiceImpl;
import com.bb3.bodybuddybe.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;
    private final EmailServiceImpl emailService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        userService.signup(requestDto);
        return ResponseEntity.ok(new ApiResponseDto("회원가입 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/email-verification")
    public ResponseEntity<ApiResponseDto> sendVerificationCode(@RequestBody @Valid EmailRequestDto requestDto) {
        emailService.sendVerificationCode(requestDto);
        return ResponseEntity.ok(new ApiResponseDto("이메일 인증 코드 전송 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponseDto> verifyCode(@RequestBody @Valid EmailVerificationRequestDto requestDto) {
        emailService.verifyCode(requestDto);
        return ResponseEntity.ok(new ApiResponseDto("이메일 인증 성공", HttpStatus.OK.value()));
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<ApiResponseDto> changeUserStatus(@RequestBody @Valid UserStatusRequestDto requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.changeUserStatus(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("회원 상태 변경 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/{userId}/image")
    public ResponseEntity<ApiResponseDto> uploadProfileImage(@RequestParam("file") MultipartFile file,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.uploadProfileImage(file, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("프로필 이미지 수정 성공", HttpStatus.OK.value()));
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<ApiResponseDto> updateProfile(@RequestBody @Valid ProfileUpdateRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateProfile(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("프로필 수정 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<ProfileResponseDto> getCurrentUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileResponseDto profileResponseDto = userService.getCurrentUserProfile(userDetails.getUser());
        return ResponseEntity.ok(profileResponseDto);
    }
}
