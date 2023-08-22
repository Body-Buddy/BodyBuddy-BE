package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.chat.entity.MessageType;
import lombok.Getter;

@Getter
public class MessageDto {

    private MessageType type;
    private Long chatId;
    private String senderNickname;
    private String content;

    public MessageDto(Message message) {
        this.type = message.getType();
        this.chatId = message.getChat().getId();
        this.senderNickname = message.getUser().getNickname();
        this.content = message.getContent();
    }

    public void changeEnterMessage(String enterNickname) {
        this.content = enterNickname + "님이 입장했습니다.";
    }
}
