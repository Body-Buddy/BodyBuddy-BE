package com.bb3.bodybuddybe.chat.advice;

import com.bb3.bodybuddybe.chat.dto.MessageRequestDto;
import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.repository.ChatRepository;
import com.bb3.bodybuddybe.chat.service.ChatService;
import com.bb3.bodybuddybe.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    // 웹소켓을 통해 서버로 json형태의 메세지 요청
    // 메세지 엔티티로 파싱하여 핸들링
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
        throws Exception {
        String payload = message.getPayload();
        log.info("{}", payload);
        MessageRequestDto chatMessage = objectMapper.readValue(payload, MessageRequestDto.class);



        Chat chatRoom = chatRepository.findById(chatMessage.getChatId()).orElseThrow(() ->
            new IllegalArgumentException("해당 채팅방이 없습니다."));


    }
}
