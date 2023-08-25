package com.bb3.bodybuddybe.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDto {

    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String email;
}