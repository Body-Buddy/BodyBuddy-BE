package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.ChatType;
import lombok.Getter;

@Getter
public class ChatRequestDto {
    private String roomName;
    private ChatType chatType;
}
