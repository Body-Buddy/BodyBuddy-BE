//package com.bb3.bodybuddybe.chat.advice;
//
//import com.bb3.bodybuddybe.chat.dto.MessageRequestDto;
//import com.bb3.bodybuddybe.chat.entity.Chat;
//import com.bb3.bodybuddybe.chat.entity.MessageType;
//import com.bb3.bodybuddybe.chat.service.ChatService;
//import com.bb3.bodybuddybe.common.exception.CustomException;
//import com.bb3.bodybuddybe.common.exception.ErrorCode;
//import com.bb3.bodybuddybe.user.entity.User;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class WebSocketHandler extends TextWebSocketHandler {
//
//    private final ObjectMapper objectMapper;
//    private final ChatService chatService;
//    private final SessionManager sessionManager;
//
//    /*
//     * Json형태의 입력값과 해당 클라이언트 세션 받아와서 로직수행을 시작 하는 곳.
//     * MessageRequestDto필드대로 입력해준다.
//     */
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message)
//        throws Exception {
//
//        String payload = message.getPayload();
//        log.info("{}", payload);
//        MessageRequestDto chatMessage = objectMapper.readValue(payload, MessageRequestDto.class);
//
//        // 사용자, 채팅방 check
//        User sendUser = chatService.findUser(chatMessage.getSenderId());
//        Chat chat = chatService.findChat(chatMessage.getChatId());
//        chatService.validateChatIsMyGym(sendUser, chat.getGym());
//
//        // 채팅방 참여완료 했는지 (UserChat에 있는지) check
//        chatService.validateJoinedChat(sendUser, chat);
//
//        handlerActions(session, chatMessage, sendUser, chat);
//
//    }
//
//    /*
//     * 클라이언트 연결 종료됐을 시 수행되는 곳.
//     * 나갔다는 알림 보내주기
//     */
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//
//        User user = chatService.findUser((Long) session.getAttributes().get("userId"));
//
//        Long chatId = sessionManager.deleteSessionAndGetChatId(session);
//
//        MessageRequestDto message = MessageRequestDto.builder()
//            .chatId(chatId)
//            .senderId(user.getId())
//            .content("")
//            .build();
//
//        message.changeLeaveMessage(user);
//
//        // 종료한 유저 나갔다는 알림 보내주기
//        sendMessage(chatId, message);
//
//    }
//
//    private void handlerActions(WebSocketSession session, MessageRequestDto chatMessage, User user, Chat chat) {
//
//        if (chatMessage.getType().equals(MessageType.ENTER)) {
//
//            checkDuplicatedEnter(session, chatMessage, user, chat);
//
//        } else if (chatMessage.getType().equals(MessageType.TALK)) {
//
//            checkEnterIsPresent(session);
//
//            // TALK일 경우 message 저장
//            chatService.saveMessage(chatMessage, user, chat);
//
//            sendMessage(chat.getId(), chatMessage);
//        }
//
//    }
//
//    private void checkEnterIsPresent(WebSocketSession session) {
//        if (session.getAttributes().isEmpty()) {    // ENTER(입장) 없이 TALK 요청 할 경우
//            throw new CustomException(ErrorCode.NEED_ENTER);
//        }
//    }
//
//    private void checkDuplicatedEnter(WebSocketSession session, MessageRequestDto chatMessage,
//        User user, Chat chat) {
//        if (sessionManager.isEntered(session)) {   // <- ENTER 중복요청 시
//            alreadyExistEnter(session, chatMessage);
//        } else {
//            newEnterSession(session, chatMessage, user, chat);
//        }
//    }
//
//    private void newEnterSession(WebSocketSession session, MessageRequestDto chatMessage, User user, Chat chat) {
//
//        // session의 key, value 값으로 userId 넣어주기.
//        session.getAttributes().put("userId", user.getId());
//
//        // 채팅 볼 수 있도록 sessions에 추가
//        sessionManager.addSession(chat.getId(), session);
//
//        // 입장했다는 알림으로 바꾸기.
//        chatMessage.changeEnterMessage(user.getNickname());
//
//        // 해당 채팅방 이전대화내용 불러오기
//        chatService.getMessages(session, chatMessage.getChatId());
//
//        sendMessage(chat.getId(), chatMessage);
//    }
//
//    private void alreadyExistEnter(WebSocketSession session, MessageRequestDto chatMessage){
//
//        // 이미 입장했다는 알림으로 바꾸기.
//        chatMessage.changeAlreadyEntered();
//
//        chatService.sendMessage(session, chatMessage);
//    }
//
//    private void sendMessage(Long chatId, MessageRequestDto message) {
//        sessionManager.getSessionsByChatId(chatId)
//            .parallelStream()
//            .forEach(session -> chatService.sendMessage(session, message));
//
//    }
//}
