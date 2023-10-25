package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.user.entity.User;
import lombok.Getter;

@Getter
public class ChatParticipantDto {
    private Long id;
    private String nickname;
    private String profileImage;

    public ChatParticipantDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.profileImage = user.getImageUrl();
    }
}
