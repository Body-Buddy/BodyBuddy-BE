package com.bb3.bodybuddybe.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequestDto {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @Size(max = 500, message = "소개글은 최대 500자까지 작성 가능합니다.")
    private String introduction;
}
