package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.chat.entity.MessageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRequestDto {

    private MessageType type;
    private String chatId;
    private String senderNickname;
    private String content;

    public MessageRequestDto(MessageType type, String chatId, String senderNickname, String content) {
        this.type = type;
        this.chatId = chatId;
        this.senderNickname = senderNickname;
        this.content = content;
    }

    public MessageRequestDto(Message message) {
        this.type = message.getType();
        this.chatId = message.getChat().getId().toString();
        this.senderNickname = message.getUser().getNickname();
        this.content = message.getContent();
    }

    public void changeEnterMessage(String enterNickname) {
        this.content = enterNickname + "님이 입장했습니다.";
    }

    public void changeAlreadyEntered(String enterNickname) {
        this.content = enterNickname + "님은 이미 입장하셨습니다.";
    }

}
