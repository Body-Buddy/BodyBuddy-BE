package com.bb3.bodybuddybe.chat.service;

import com.bb3.bodybuddybe.chat.dto.ChatRequestDto;
import com.bb3.bodybuddybe.chat.dto.ChatResponseDto;
import com.bb3.bodybuddybe.chat.dto.MessageRequestDto;
import com.bb3.bodybuddybe.chat.dto.MessageResponseDto;
import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.chat.entity.UserChat;
import com.bb3.bodybuddybe.chat.repository.ChatRepository;
import com.bb3.bodybuddybe.chat.repository.MessageRepository;
import com.bb3.bodybuddybe.chat.repository.UserChatRepository;
import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.gym.repository.UserGymRepository;
import com.bb3.bodybuddybe.user.entity.User;
import com.bb3.bodybuddybe.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ObjectMapper objectMapper;
    private final ChatRepository chatRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final UserGymRepository userGymRepository;
    private final UserChatRepository userChatRepository;


    /*
     * 채팅방 CRUD
     */
    @Transactional
    public ChatResponseDto createChatRoom(User user, ChatRequestDto chatRequestDto, Long gymId) {

        Gym gym = findGym(gymId);

        validateUserMembership(user, gym);

        Chat chat = Chat.builder()
            .roomName(chatRequestDto.getRoomName())
            .chatType(chatRequestDto.getChatType())
            .gym(gym)
            .ownerUser(user)
            .build();

        UserChat userChat = new UserChat(user, chat);

        chatRepository.save(chat);
        userChatRepository.save(userChat);

        return new ChatResponseDto(chat);

    }

    public List<ChatResponseDto> getAllChatsByGym(Long gymId) {
        return chatRepository.findAllByGym_Id(gymId)
            .stream()
            .map(ChatResponseDto::new)
            .collect(Collectors.toList());
    }

    public List<ChatResponseDto> getMyChats(Long gymId, User user) {
        return userChatRepository.findAllByUserAndChat_Gym_Id(user, gymId)
            .stream()
            .map(UserChat::getChat)
            .map(ChatResponseDto::new)
            .toList();
    }

    @Transactional
    public void updateChat(User user, Long chatId, ChatRequestDto chatRequestDto) {
        Chat chat = findChat(chatId);
        validateUserIsOwner(user, chat);

        chat.updateChat(chatRequestDto.getChatType(), chatRequestDto.getRoomName());

        chatRepository.save(chat);
    }

    @Transactional
    public void deleteChat(User user, Long chatId) {
        Chat chat = findChat(chatId);
        validateUserIsOwner(user, chat);

        chatRepository.delete(chat);
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    /*
     * 채팅방 참여 / 나가기
     */

    public void joinChat(User user, Long chatId) {
        Chat chat = findChat(chatId);
        validateDuplicatedUserChat(user, chat);
        UserChat userChat = new UserChat(user, chat);

        userChatRepository.save(userChat);
    }

    public void leaveChat(User user, Long chatId) {
        Chat chat = findChat(chatId);

        ownerCantLeave(user, chat);

        UserChat userChat = findUserChat(user, chatId);

        userChatRepository.delete(userChat);
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    /*
     * 채팅 메세지관련
     */

    @Transactional
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(
                new TextMessage(convertMessageToJson((MessageResponseDto) message)));
        } catch (IOException e) {
            log.error("Error sending message to session: " + session.getId(), e);
            throw new CustomException(ErrorCode.WEBSOCKET_SEND_ERROR);
        }
    }

    @Transactional
    public MessageResponseDto saveMessage(MessageRequestDto message, User user, Chat chat) {

        Message chatMessage = Message.builder()
            .type(message.getType())
            .content(message.getContent())
            .chat(chat)
            .user(user)
            .build();

        messageRepository.save(chatMessage);

        return new MessageResponseDto(chatMessage);

    }

    public void getMessages(WebSocketSession session, Long chatId) {
        List<MessageResponseDto> messageResponseDtos = messageRepository.findAllByChatId(chatId)
            .stream()
            .map(MessageResponseDto::new)
            .collect(Collectors.toList());

        messageResponseDtos.forEach(messageResponseDto -> {
            try {
                session.sendMessage(new TextMessage(convertMessageToJson(messageResponseDto)));
            } catch (IOException e) {
                log.error("Error sending message to session: " + session.getId(), e);
                throw new CustomException(ErrorCode.WEBSOCKET_SEND_ERROR);
            }
        });


    }

//--------------------------------------------------------------------------------------------------------------------------------------------


    private Gym findGym(Long gymId) {
        return gymRepository.findById(gymId)
            .orElseThrow(() -> new CustomException(ErrorCode.GYM_NOT_FOUND));
    }

    private Chat findChat(Long chatId) {
        return chatRepository.findById(chatId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_NOT_FOUND));
    }

    private void validateUserMembership(User user, Gym gym) {
        if (!userGymRepository.existsByUserAndGym(user, gym)) {
            throw new CustomException(ErrorCode.NOT_MY_GYM);
        }
    }

    private void validateDuplicatedUserChat(User user, Chat chat) {
        if (userChatRepository.existsByChatAndUser(chat, user)) {
            throw new CustomException(ErrorCode.DUPLICATED_USERCHAT);
        }
    }

    private void validateUserIsOwner(User user, Chat chat) {
        if (chat.getOwnerUser().getId()!=user.getId()) {
            throw new CustomException(ErrorCode.USER_NOT_CHAT_OWNER);
        }
    }

    private void ownerCantLeave(User user, Chat chat) {
        if (chat.getOwnerUser().getId()==user.getId()) {
            throw new CustomException(ErrorCode.OWNER_CAN_NOT_LEAVE);
        }
    }

    private UserChat findUserChat(User user, Long chatId) {
        return userChatRepository.findByUserAndChatId(user, chatId)
            .orElseThrow(() -> new CustomException(ErrorCode.USERCHAT_NOT_FOUND));
    }

    private String convertMessageToJson(MessageResponseDto message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error("Error converting message to JSON", e);
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

}
