package com.bb3.bodybuddybe.chat.dto;

import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.user.entity.User;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRequestDto {

    private Long chatId;
    private Long senderId;
    private String content;

    @Builder
    public MessageRequestDto(Long chatId, Long senderId, String content) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
    }

    public MessageRequestDto(Message message) {
        this.chatId = message.getChat().getId();
        this.senderId = message.getUser().getId();
        this.content = message.getContent();
    }

    public String changeEnterMessage(String enterNickname) {
        this.content = enterNickname + "님이 입장했습니다.";
        return content;
    }
    public String changeExitMessage(String enterNickname) {
        this.content = enterNickname + "님이 퇴장했습니다.";
        return content;
    }

    public MessageResponseDto changeToResponseDto(User user) {
        return MessageResponseDto.builder()
            .chatId(this.chatId)
            .senderNickname(user.getNickname())
            .content(this.content)
            .messageDate(LocalDateTime.now())
            .build();
    }
}
