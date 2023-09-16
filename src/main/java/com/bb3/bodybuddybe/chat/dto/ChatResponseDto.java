package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.enums.ChatType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ChatResponseDto {
    private Long id;
    private String name;
    private ChatType chatType;
    private List<ChatParticipantDto> participants;
    private LocalDateTime createdAt;
    private MessageResponseDto lastMessage;

    public ChatResponseDto(Chat chat) {
        this.id = chat.getId();
        this.name = chat.getName();
        this.chatType = chat.getChatType();
        this.participants = chat.getParticipants()
                .stream()
                .map(participant -> new ChatParticipantDto(participant.getUser()))
                .toList();
        this.createdAt = chat.getCreatedAt();
        this.lastMessage = chat.getMessages().isEmpty() ? null : new MessageResponseDto(chat.getMessages().get(chat.getMessages().size() - 1));
    }
}
