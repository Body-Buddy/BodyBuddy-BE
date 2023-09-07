package com.bb3.bodybuddybe.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private Long userId;
    private boolean isNewUser;
}
