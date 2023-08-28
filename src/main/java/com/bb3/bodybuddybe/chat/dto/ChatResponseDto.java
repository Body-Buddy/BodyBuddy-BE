package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.ChatType;
import java.util.List;
import lombok.Getter;

@Getter
public class ChatResponseDto {
    private Long roomId;
    private String roomName;
    private ChatType chatType;


    public ChatResponseDto(Long roomId, String roomName, ChatType chatType) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.chatType = chatType;
    }




}
