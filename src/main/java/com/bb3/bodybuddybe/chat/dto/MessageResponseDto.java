package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.Message;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageResponseDto {

    private Long chatId;
    private String senderNickname;
    private String content;
    private LocalDateTime messageDate;

    public MessageResponseDto(Message message) {
        this.chatId = message.getChat().getId();
        this.senderNickname = message.getUser().getNickname();
        this.content = message.getContent();
        this.messageDate = message.getCreatedAt();
    }

    @Builder
    public MessageResponseDto(Long chatId, String senderNickname, String content, LocalDateTime messageDate) {
        this.chatId = chatId;
        this.senderNickname = senderNickname;
        this.content = content;
        this.messageDate = messageDate;
    }
}
