package com.bb3.bodybuddybe.user.dto;

import com.bb3.bodybuddybe.user.entity.User;
import lombok.Getter;

@Getter
public class AuthorResponseDto {
    private Long id;
    private String nickname;
    private String profileImage;

    public AuthorResponseDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.profileImage = user.getImageUrl();
    }
}
