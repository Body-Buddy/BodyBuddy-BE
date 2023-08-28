package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.chat.entity.MessageType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageResponseDto {

    private MessageType type;
    private String chatId;
    private String senderNickname;
    private String content;
    private LocalDateTime messageDate;

    public MessageResponseDto(Message message) {
        this.type = message.getType();
        this.chatId = message.getChat().getId().toString();
        this.senderNickname = message.getUser().getNickname();
        this.content = message.getContent();
        this.messageDate = message.getCreatedAt();
    }

}
