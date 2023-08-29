package com.bb3.bodybuddybe.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeleteRequestDto {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
