package com.bb3.bodybuddybe.users.dto;

import lombok.Getter;

@Getter
public class UserInfoDto {
    private String username;
    //  boolean isAdmin;
    private boolean isOwner;

    public UserInfoDto(String username, boolean isOwner) {
        this.username = username;
        this.isOwner = isOwner;
    }
}
