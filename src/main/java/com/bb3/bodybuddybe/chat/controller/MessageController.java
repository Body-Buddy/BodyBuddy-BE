package com.bb3.bodybuddybe.chat.controller;

import com.bb3.bodybuddybe.chat.dto.MessageRequestDto;
import com.bb3.bodybuddybe.chat.dto.MessageResponseDto;
import com.bb3.bodybuddybe.chat.service.MessageService;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    /*
     * 채팅방 입장
     */
    @MessageMapping("/messages/participation/{chatId}") // -> /pub/messages/participation/{chatId}
    @SendTo("/sub/messages/{chatId}")
    public MessageResponseDto participation(@DestinationVariable Long chatId, MessageRequestDto messageRequestDto,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return messageService.participation(chatId, messageRequestDto, userDetails.getUser());
    }


    /*
     * 채팅입력
     */
    @MessageMapping("/messages/{chatId}")
    @SendTo("/sub/messages/{chatId}")
    public MessageResponseDto sendMessage(@DestinationVariable Long chatId, MessageRequestDto messageRequestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return messageService.sendMessage(chatId, messageRequestDto, userDetails.getUser());

    }

    /*
     * 채팅방 퇴장
     */
    @MessageMapping("/messages/exit/{chatId}") // -> /pub/messages/exit/{chatId}
    @SendTo("/sub/messages/{chatId}")
    public MessageResponseDto exit(@DestinationVariable Long chatId, MessageRequestDto messageRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return messageService.exit(chatId, messageRequestDto, userDetails.getUser());
    }

    /*
     * 이전 대화내용 불러오기. (채팅방 입장 후 버튼형식)
     */
    @GetMapping("/messages/past/{chatId}")
    @ResponseBody
    public ResponseEntity<List<MessageResponseDto>> getPastMessages(@PathVariable Long chatId) {
        List<MessageResponseDto> response = messageService.getPastMessages(chatId);
        return ResponseEntity.ok(response);
    }

}
