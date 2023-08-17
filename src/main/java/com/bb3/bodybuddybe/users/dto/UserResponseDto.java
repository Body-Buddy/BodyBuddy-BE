package com.bb3.bodybuddybe.users.dto;

import com.bb3.bodybuddybe.users.entity.Users;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private String username;
    public UserResponseDto(Users user) {
        this.username = user.getUsername();
    }
}
