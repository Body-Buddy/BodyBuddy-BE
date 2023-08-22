package com.bb3.bodybuddybe.chat.dto;

import lombok.Getter;

@Getter
public class MessageResponseDto {

    private Long chatId;
    private String senderNickName;
    private String content;

    public MessageResponseDto(String nickName, String content) {
        this.senderNickName = nickName;
        this.content = content;
    }
}
