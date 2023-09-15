package com.bb3.bodybuddybe.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChatRequestDto {

    @NotBlank(message = "채팅방 이름을 입력해주세요.")
    private String name;
}
