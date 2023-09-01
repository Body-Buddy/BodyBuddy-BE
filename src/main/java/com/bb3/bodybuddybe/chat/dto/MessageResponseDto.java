package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.chat.entity.MessageType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageResponseDto {

    private MessageType type;
    private Long chatId;
    private String senderNickname;
    private String content;
    private LocalDateTime messageDate;

    public MessageResponseDto(Message message) {
        this.type = message.getType();
        this.chatId = message.getChat().getId();
        this.senderNickname = message.getUser().getNickname();
        this.content = message.getContent();
        this.messageDate = message.getCreatedAt();
    }

    @Builder
    public MessageResponseDto(MessageType type, Long chatId, String senderNickname,
        String content,
        LocalDateTime messageDate) {
        this.type = type;
        this.chatId = chatId;
        this.senderNickname = senderNickname;
        this.content = content;
        this.messageDate = messageDate;
    }
}
