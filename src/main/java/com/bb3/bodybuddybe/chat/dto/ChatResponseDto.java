package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.entity.ChatType;
import com.bb3.bodybuddybe.user.entity.User;
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

    // 1대1 채팅방용 생성자
    public ChatResponseDto(Long roomId, String roomname, ChatType chatType, User user) {
        this.roomId = roomId;
        this.roomname = user.getNickname() + roomname;
        this.chatType = chatType;
    }




}
