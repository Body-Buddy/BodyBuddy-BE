package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.Message;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {
    private Long id;
    private String content;
    private LocalDateTime sentAt;
    private ChatParticipantDto sender;

    public MessageResponseDto(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.sentAt = message.getSentAt();
        this.sender = new ChatParticipantDto(message.getSender());
    }
}
