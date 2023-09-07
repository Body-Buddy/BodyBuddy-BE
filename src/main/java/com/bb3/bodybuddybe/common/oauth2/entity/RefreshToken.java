package com.bb3.bodybuddybe.common.oauth2.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 7)
public class RefreshToken {

    @Id
    private String refreshToken;

    private Long userId;

    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
