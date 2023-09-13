package com.bb3.bodybuddybe.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequestDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@!%*#?&]).{8,15}$", message = "비밀번호는 최소 8자 이상, 15자 이하이며 영문 대소문자, 숫자, 특수문자가 각 1개 이상 포함되어야 합니다.")
    private String password;
}
