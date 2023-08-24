package com.bb3.bodybuddybe.gym.dto;

import com.bb3.bodybuddybe.gym.entity.Gym;
import lombok.Getter;

@Getter
public class GymResponseDto {
    private Long id;
    private String kakaoPlaceId;
    private String name;
    private String roadAddress;
    private Integer memberCount;

    public GymResponseDto(Gym gym) {
        this.id = gym.getId();
        this.kakaoPlaceId = gym.getKakaoPlaceId();
        this.name = gym.getName();
        this.roadAddress = gym.getRoadAddress();
        this.memberCount = gym.getMembers().size();
    }
}
