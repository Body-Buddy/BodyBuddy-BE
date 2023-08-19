package com.bb3.bodybuddybe.user.dto;

import com.bb3.bodybuddybe.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private String username;
    public UserResponseDto(User user) {
        this.username = user.getUsername();
    }
}
