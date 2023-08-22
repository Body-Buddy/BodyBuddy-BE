package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.MessageType;
import lombok.Getter;

@Getter
public class MessageRequestDto {

    private MessageType type;
    private Long chatId;
    private String senderNickName;
    private String content;

}
