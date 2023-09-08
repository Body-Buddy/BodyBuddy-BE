package com.bb3.bodybuddybe.user.dto;

import com.bb3.bodybuddybe.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String nickname;
    private boolean hasRegisteredGym;
    private boolean hasSetProfile;
    private boolean hasSetMatchingCriteria;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.hasRegisteredGym = user.getHasRegisteredGym();
        this.hasSetProfile = user.getHasSetProfile();
        this.hasSetMatchingCriteria = user.getHasSetMatchingCriteria();
    }
}
