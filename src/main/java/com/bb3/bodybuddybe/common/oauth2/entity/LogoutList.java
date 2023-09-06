package com.bb3.bodybuddybe.common.oauth2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "logoutlist") //로그아웃된 회원
@Getter
@AllArgsConstructor
public class LogoutList {
    @Id
    private String accessToken;

    @TimeToLive
    private Long expirationSeconds;
}
