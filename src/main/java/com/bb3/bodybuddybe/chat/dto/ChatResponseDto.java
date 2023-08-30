package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.entity.ChatType;
import lombok.Getter;

@Getter
public class ChatResponseDto {
    private Long roomId;
    private String roomname;
    private ChatType chatType;


    public ChatResponseDto(Chat chat) {
        this.roomId = chat.getId();
        this.roomname = chat.getRoomname();
        this.chatType = chat.getChatType();
    }




}
