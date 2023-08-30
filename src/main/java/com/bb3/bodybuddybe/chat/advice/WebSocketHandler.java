package com.bb3.bodybuddybe.chat.advice;

import com.bb3.bodybuddybe.chat.dto.MessageRequestDto;
import com.bb3.bodybuddybe.chat.dto.MessageResponseDto;
import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.entity.MessageType;
import com.bb3.bodybuddybe.chat.repository.ChatRepository;
import com.bb3.bodybuddybe.chat.service.ChatService;
import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.gym.repository.UserGymRepository;
import com.bb3.bodybuddybe.user.entity.User;
import com.bb3.bodybuddybe.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
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
    private final UserGymRepository userGymRepository;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
        throws Exception {

        String payload = message.getPayload();
        log.info("{}", payload);
        MessageRequestDto chatMessage = objectMapper.readValue(payload, MessageRequestDto.class);

        // 사용자, 채팅방 check
        User user = userRepository.findByNickname(chatMessage.getSenderNickname())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Chat chat = chatRepository.findById(Long.parseLong(chatMessage.getChatId()))
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_NOT_FOUND));
        if (!userGymRepository.existsByUserAndGymId(user, chat.getGym().getId())) {
            throw new CustomException(ErrorCode.CHAT_NOT_MY_GYM);
        }

        handlerActions(session, chatMessage, user, chat);

    }

    /*
     * 클라이언트 연결 close 시 sessions에서 해당 session 제거
     * 나갔다는 알림 보내주기
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        // 삭제 전 sessions 확인 forEach 출력 (test)
        sessions.forEach(socketSession -> {
            Map<String, Object> attributes = socketSession.getAttributes();
            System.out.println("삭제 전 session attributes : " + attributes);
        });

        sessions.remove(session);

        // 삭제 후 sessions 확인 forEach 출력 (test)
        sessions.forEach(socketSession -> {
            Map<String, Object> attributes = socketSession.getAttributes();
            System.out.println("삭제 후 session attributes : " + attributes);
        });

        // 종료한 유저 나갔다는 알림 보내주기
        User user = userRepository.findById((Long) session.getAttributes().get("userId"))
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        sendMessage(user.getNickname() + "님이 나갔습니다.");

    }

    private void handlerActions(WebSocketSession session, MessageRequestDto chatMessage, User user, Chat chat) {

        if (chatMessage.getType().equals(MessageType.ENTER)) {
            if (sessions.contains(session)) {   // <- ENTER 중복요청 시
                alreadyExistEnter(session, chatMessage);
            } else {
                newEnterSession(session, chatMessage, user, chat);
            }

        } else if (chatMessage.getType().equals(MessageType.TALK)) {

            // session에 담긴 userId 출력하여 확인 (test)
            System.out.println("TALK, put되었던 session 출력 : " + session.getAttributes());

            if (session.getAttributes().isEmpty()) {    // ENTER(입장) 없이 TALK 요청 할 경우
                throw new CustomException(ErrorCode.NEED_ENTER);
            }

            // TALK일 경우 message 저장
            MessageResponseDto messageResponseDto = chatService.saveMessage(chatMessage, user, chat);

            sendMessage(messageResponseDto);
        }

    }

    private <T> void sendMessage(T message) {
        sessions.parallelStream()
            .forEach(session -> chatService.sendMessage(session, message));

    }

    private void newEnterSession(WebSocketSession session, MessageRequestDto chatMessage, User user, Chat chat) {

        // session의 key, value 값으로 userId 넣어주기.
        session.getAttributes().put("userId", user.getId());

        // 채팅 볼 수 있도록 sessions에 추가
        sessions.add(session);

        // 입장했다는 알림으로 바꾸기.
        chatMessage.changeEnterMessage(chatMessage.getSenderNickname());

        // 해당 채팅방 이전대화내용 불러오기
        chatService.getMessages(session, Long.parseLong(chatMessage.getChatId()));

        sendMessage(chatMessage);
    }

    private void alreadyExistEnter(WebSocketSession session, MessageRequestDto chatMessage){

        // 이미 입장했다는 알림으로 바꾸기.
        chatMessage.changeAlreadyEntered(chatMessage.getSenderNickname());

        // 이미 입장했다는 메세지는 본인에게만 보여지면 되므로 해당 session에만 send 해줌.
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
        } catch (IOException e) {
            new IOException(e.getMessage());
        }
    }
}
