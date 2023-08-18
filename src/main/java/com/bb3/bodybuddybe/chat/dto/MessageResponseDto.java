package com.bb3.bodybuddybe.chat.dto;

import lombok.Getter;

@Getter
public class MessageResponseDto {

    private String nickName;
    private String content;

    public MessageResponseDto(String nickName, String content) {
        this.nickName = nickName;
        this.content = content;
    }
}
