package com.bb3.bodybuddybe.user.dto;

import com.bb3.bodybuddybe.matching.enums.GenderEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class  SignupRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@!%*#?&]).{8,15}$", message = "비밀번호는 최소 8자 이상, 15자 이하이며 영문 대소문자, 숫자, 특수문자가 각 1개 이상 포함되어야 합니다.")
    private String password;

    @NotNull(message = "성별을 선택해주세요.")
    private GenderEnum gender;

    @NotNull(message = "생년월일을 입력해주세요.")
    @Past(message = "생년월일은 오늘 날짜보다 이전이어야 합니다.")
    private LocalDate birthDate;
}
