package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.ChatType;
import lombok.Getter;

@Getter
public class ChatResponseDto {
    private String roomName;
    private ChatType chatType;

    public ChatResponseDto(String roomName, ChatType chatType) {
        this.roomName = roomName;
        this.chatType = chatType;
    }
}
