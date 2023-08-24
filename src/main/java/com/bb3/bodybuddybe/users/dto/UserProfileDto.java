package com.bb3.bodybuddybe.users.dto;

import com.bb3.bodybuddybe.users.entity.Users;
import lombok.Getter;

@Getter
public class UserProfileDto {
    private String imageUrl;
    private String email;
    private String introduction;

    public UserProfileDto(Users users) {
        this.imageUrl = users.getImageUrl();
        this.email = users.getEmail();
        this.introduction = users.getIntroduction();
    }
}
