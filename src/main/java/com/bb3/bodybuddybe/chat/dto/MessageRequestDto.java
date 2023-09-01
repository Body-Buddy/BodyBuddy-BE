package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.chat.entity.MessageType;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRequestDto {

    private MessageType type;
    private Long chatId;
    private Long senderUserId;
    private String content;

    @Builder
    public MessageRequestDto(MessageType type, Long chatId, Long senderUserId, String content) {
        this.type = type;
        this.chatId = chatId;
        this.senderUserId = senderUserId;
        this.content = content;
    }

    public MessageRequestDto(Message message) {
        this.type = message.getType();
        this.chatId = message.getChat().getId();
        this.senderUserId = message.getUser().getId();
        this.content = message.getContent();
    }

    public void changeEnterMessage(String enterNickname) {
        this.content = enterNickname + "님이 입장했습니다.";
    }

    public void changeAlreadyEntered() {
        this.content = "이미 입장하셨습니다.";
    }

    public void changeLeaveMessage(User user) {
        this.content = user.getNickname() + "님이 나갔습니다.";
    }
}
