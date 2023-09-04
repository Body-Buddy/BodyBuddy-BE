package com.bb3.bodybuddybe.user.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private Long userId;
    private boolean profileExists;

    public LoginResponseDto(Long userId, boolean profileExists) {
        this.userId = userId;
        this.profileExists = profileExists;
    }
}
