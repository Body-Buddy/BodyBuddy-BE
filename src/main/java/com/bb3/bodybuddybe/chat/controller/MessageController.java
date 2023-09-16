package com.bb3.bodybuddybe.chat.controller;

import com.bb3.bodybuddybe.chat.dto.MessageRequestDto;
import com.bb3.bodybuddybe.chat.dto.MessageResponseDto;
import com.bb3.bodybuddybe.chat.service.MessageService;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @ResponseBody
    @GetMapping("/api/chats/{chatId}/messages")
    public ResponseEntity<List<MessageResponseDto>> getPastMessages(@PathVariable Long chatId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<MessageResponseDto> messages = messageService.getPastMessages(chatId, userDetails.getUser());
        return ResponseEntity.ok(messages);
    }

    @MessageMapping("/chats/{chatId}/send")
    @SendTo("/sub/chats/{chatId}")
    public MessageResponseDto sendMessage(@DestinationVariable Long chatId, MessageRequestDto requestDto) {
        return messageService.sendMessage(chatId, requestDto);
    }

    @MessageMapping("/chats/{chatId}/enter")
    @SendTo("/sub/chats/{chatId}")
    public MessageResponseDto sendEnterMessage(@DestinationVariable Long chatId, MessageRequestDto requestDto) {
        return messageService.sendEnterMessage(chatId, requestDto);
    }

    @MessageMapping("/chats/{chatId}/leave")
    @SendTo("/sub/chats/{chatId}")
    public MessageResponseDto sendLeaveMessage(@DestinationVariable Long chatId, MessageRequestDto requestDto) {
        return messageService.sendLeaveMessage(chatId, requestDto);
    }
}
