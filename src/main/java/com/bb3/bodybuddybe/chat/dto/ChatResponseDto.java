package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.ChatType;
import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.chat.service.ChatService;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class ChatResponseDto {
    private Long roomId;
    private String roomName;
    private ChatType chatType;
    private Set<WebSocketSession> sessions = new HashSet<>();


    public ChatResponseDto(Long roomId, String roomName, ChatType chatType) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.chatType = chatType;
    }

    public void handlerActions(WebSocketSession session, Message message, ChatService chatService) {

    }
}
