package com.bb3.bodybuddybe.user.controller;

import com.bb3.bodybuddybe.common.advice.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.user.dto.*;
import com.bb3.bodybuddybe.user.service.EmailServiceImpl;
import com.bb3.bodybuddybe.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

//카프카, 엘라스틱 서치, 레디스
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl usersServiceimpl;
    private final EmailServiceImpl emailServiceimpl;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody AuthRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            throw new IllegalArgumentException();
        }
        usersServiceimpl.signup(requestDto);
        return ResponseEntity.ok(new ApiResponseDto("회원가입 완료",200));
    }



    //프로필수정
    @PutMapping( value = "/{userId}/profile",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponseDto> changeUserInfo(@RequestPart("profilePic") MultipartFile profilePic,
                                                         @RequestPart("introduction") String introduction,
                                                         @RequestPart("password") String password, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        if (!usersServiceimpl.isValidString(password)) {
            throw new IllegalArgumentException("비밀번호 양식을 확인해주세요.");
        }
        usersServiceimpl.changeUserInfo(profilePic, introduction, password, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("회원수정 완료", HttpStatus.CREATED.value()));

    }


    //프로필조회
    @GetMapping("/{userId}/profile")
    public UserProfileDto getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        UserProfileDto userProfileDto = usersServiceimpl.getUserProfile(userDetails.getUser());
        return userProfileDto;
    }

    //특정 사용자 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponseDto> getOneUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return usersServiceimpl.getProfile(userDetails.getUser());
    }


    //메일인증
    @PostMapping("/login/mailConfirm")
    public String mailConfirm(@RequestBody EmailAuthRequestDto requestDto) throws Exception {
        return  emailServiceimpl.sendSimpleMessage(requestDto.getEmail());
    }

    //회원 탈퇴
    @DeleteMapping("/withdrawal")
    public ResponseEntity<ApiResponseDto> delete(@RequestBody DeleteRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        usersServiceimpl.delete(requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("회원탈퇴 완료", 200));

    }
}
