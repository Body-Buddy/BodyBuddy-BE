package com.bb3.bodybuddybe.user.dto;

import com.bb3.bodybuddybe.user.entity.User;
import lombok.Getter;

@Getter
public class UserProfileDto {
    private Long id;
    private String imageUrl;
    private String email;
    private String introduction;

    public UserProfileDto(User user) {
        this.id = user.getId();
        this.imageUrl = user.getImageUrl();
        this.email = user.getEmail();
        this.introduction = user.getIntroduction();
    }
}
