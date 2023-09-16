package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDto {
    private Long senderId;
    private MessageType type;
    private String content;
}
