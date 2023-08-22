package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.ChatType;
import com.bb3.bodybuddybe.chat.entity.MessageType;
import com.bb3.bodybuddybe.chat.service.ChatService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class ChatResponseDto {
    private Long roomId;
    private String roomName;
    private ChatType chatType;
    private List<MessageDto> messageDtos;
    private Set<WebSocketSession> sessions = new HashSet<>();


    public ChatResponseDto(Long roomId, String roomName, ChatType chatType) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.chatType = chatType;
    }

    public void handlerActions(WebSocketSession session, MessageDto chatMessage, ChatService chatService) {
        if (chatMessage.getType().equals(MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.changeEnterMessage(chatMessage.getSenderNickname());

            // 해당 채팅방 이전대화내용 불러오기
            chatService.getMessages(session, chatMessage.getChatId());
        }

        sendMessage(chatMessage, chatService);
    }

    private <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));

        // 메세지 저장
        MessageDto messageDto = (MessageDto) message;
        if (messageDto.getType().equals(MessageType.TALK)) {
            chatService.saveMessage(messageDto);
        }
    }
}
