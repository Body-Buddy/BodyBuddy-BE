package com.bb3.bodybuddybe.user.controller;


import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.oauth2.repository.LogoutlistRepository;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.user.dto.*;
import com.bb3.bodybuddybe.user.service.EmailServiceImpl;
import com.bb3.bodybuddybe.user.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserServiceImpl userService;
    private final EmailServiceImpl emailService;


    @PostMapping("/users/signup")
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {

        userService.signup(requestDto);
        return ResponseEntity.ok(new ApiResponseDto("회원가입 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/logout") // @Post 변경 예정
    public ResponseEntity logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request){
        userService.logout(userDetails.getUser(), request);
        return ResponseEntity.ok().body("로그아웃 완료");
    }



    @PostMapping("/users/social-profile")
    public ResponseEntity<ApiResponseDto> socialAddProfile(@Valid @RequestBody SocialUpdateInform requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails ) {
        userService.socialAddProfile(requestDto,userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("소셜로그인 사용자 프로필 추가 작성 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/users/change-password")
    public ResponseEntity<ApiResponseDto> changePassword(@Valid @RequestBody ChangedPasswordRequestDto requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails ) {
        userService.changePassword(requestDto,userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("비밀번호 변경 완료", HttpStatus.OK.value()));
    }

//탈퇴 휴먼 /
    @PostMapping("/email-verification/request")
    public ResponseEntity<ApiResponseDto> sendVerificationCode(@RequestBody @Valid EmailRequestDto requestDto) {
        emailService.sendVerificationCode(requestDto);
        return ResponseEntity.ok(new ApiResponseDto("이메일 인증 코드 전송 성공", HttpStatus.OK.value()));
    }


    @PostMapping("/email-verification/confirm")
    public ResponseEntity<ApiResponseDto> confirmVerification(@RequestBody @Valid EmailConfirmRequestDto requestDto) {
        emailService.confirmVerification(requestDto);
        return ResponseEntity.ok(new ApiResponseDto("이메일 인증 성공", HttpStatus.OK.value()));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponseDto> deleteUser(@RequestBody @Valid UserDeleteRequestDto requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("회원 탈퇴 성공", HttpStatus.OK.value()));
    }

    @PutMapping("/users/{userId}/image")
    public ResponseEntity<ApiResponseDto> uploadProfileImage(@RequestParam("file") MultipartFile file,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.uploadProfileImage(file, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("프로필 이미지 수정 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/users/{userId}/image")
    public ResponseEntity<String> getProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String imageUrl = userService.getProfileImage(userDetails.getUser());
        return ResponseEntity.ok(imageUrl);
    }

    @DeleteMapping("/users/{userId}/image")
    public ResponseEntity<ApiResponseDto> deleteProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteProfileImage(userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("프로필 이미지 삭제 성공", HttpStatus.OK.value()));
    }

    @PutMapping("/users/{userId}/profile")
    public ResponseEntity<ApiResponseDto> updateProfile(@RequestBody @Valid ProfileUpdateRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateProfile(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("프로필 수정 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long userId) {
        ProfileResponseDto profileResponseDto = userService.getProfile(userId);
        return ResponseEntity.ok(profileResponseDto);
    }
}
