package com.bb3.bodybuddybe.user.dto;

import com.bb3.bodybuddybe.user.entity.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private Long userId;
    private String imageUrl;
    private String nickname;
    private String introduction;

    public ProfileResponseDto(User user) {
        this.userId = user.getId();
        this.imageUrl = user.getImageUrl();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
    }
}
