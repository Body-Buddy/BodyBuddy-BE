package com.bb3.bodybuddybe.chat.service;

import com.bb3.bodybuddybe.chat.dto.ChatRequestDto;
import com.bb3.bodybuddybe.chat.dto.ChatResponseDto;
import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.entity.ChatType;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * Used in ChatController
     */
    @Transactional
    public ChatResponseDto createChatRoom(User user, ChatRequestDto chatRequestDto, Long gymId) {

        Gym gym = findGym(gymId);

        validateUserMembership(user, gym);

        Chat chat = Chat.builder()
            .roomName(chatRequestDto.getRoomName())
            .chatType(ChatType.GROUP)
            .gym(gym)
            .ownerUser(user)
            .build();

        UserChat userChat = new UserChat(chat.getOwnerUser(), chat);

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
        validateUserIsChatOwner(user, chat);

        chat.updateChat(chatRequestDto.getRoomName());

        chatRepository.save(chat);
    }

    @Transactional
    public void deleteChat(User user, Long chatId) {
        Chat chat = findChat(chatId);
        validateUserIsChatOwner(user, chat);

        chatRepository.delete(chat);
    }

    @Transactional
    public ChatResponseDto getOrCreateDirectChatRoom(Long gymId, User user, Long toChatUserId) {
        User toChatUser = findUser(toChatUserId);
        Gym gym = findGym(gymId);

        validateUserMembership(user,gym);

        List<Chat> userDirectChats = chatRepository.findAllByGym_IdAndOwnerUserAndChatTypeContains(gymId, user, ChatType.DIRECT);

        Chat directChatRoom = null;

        for (Chat userDirectChat : userDirectChats) {
            for (UserChat userChat : userDirectChat.getUserChatList()) {
                if (userChat.getUser().getId()==toChatUserId) {
                    directChatRoom = userDirectChat;
                    break;
                }
            }
        }

        // 위 for문을 타도 null값인 경우 (1대1방이 존재하지 않는경우)
        if (directChatRoom.equals(null)) {
            Chat chat = Chat.builder()
                .chatType(ChatType.DIRECT)
                .roomName("님 과 1대1 채팅방")
                .gym(gym)
                .ownerUser(user)
                .build();

            chatRepository.save(chat);

            UserChat userChat1 = new UserChat(chat.getOwnerUser(), chat);
            UserChat userChat2 = new UserChat(toChatUser, chat);

            userChatRepository.save(userChat1);
            userChatRepository.save(userChat2);

            directChatRoom = chat;
        }

        // Dto 변환
        ChatResponseDto chatResponseDto = new ChatResponseDto(
            directChatRoom.getId(),
            directChatRoom.getRoomname(),
            directChatRoom.getChatType(),
            toChatUser
        );

        return chatResponseDto;
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    /*
     * 채팅방 참여 / 나가기
     * Used in ChatController
     */

    @Transactional
    public void joinChat(User user, Long chatId) {
        Chat chat = findChat(chatId);
        validateDuplicatedUserChat(user, chat);
        UserChat userChat = new UserChat(user, chat);

        userChatRepository.save(userChat);
    }

    @Transactional
    public void leaveChat(User user, Long chatId) {
        Chat chat = findChat(chatId);

        ownerCantLeave(user, chat);

        UserChat userChat = findUserChat(user, chatId);

        userChatRepository.delete(userChat);
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    /*
     * 채팅 메세지관련
     * Used in WebSocketHandler
     */
//
//    public void sendMessage(WebSocketSession session, MessageRequestDto message) {
//        MessageResponseDto messageResponseDto = changeToResponseDto(message);
//        try {
//            session.sendMessage(
//                new TextMessage(convertMessageToJson(messageResponseDto)));
//        } catch (IOException e) {
//            log.error("Error sending message to session: " + session.getId(), e);
//            throw new CustomException(ErrorCode.WEBSOCKET_SEND_ERROR);
//        }
//    }

//    @Transactional
//    public void saveMessage(MessageRequestDto message, User user, Chat chat) {
//
//        Message chatMessage = Message.builder()
//            .content(message.getContent())
//            .chat(chat)
//            .user(user)
//            .build();
//
//        messageRepository.save(chatMessage);
//
//    }

//    public void getMessages(WebSocketSession session, Long chatId) {
//        List<MessageResponseDto> messageResponseDtos = messageRepository.findAllByChatId(chatId)
//            .stream()
//            .map(MessageResponseDto::new)
//            .collect(Collectors.toList());
//
//        messageResponseDtos.forEach(messageResponseDto -> {
//            try {
//                session.sendMessage(new TextMessage(convertMessageToJson(messageResponseDto)));
//            } catch (IOException e) {
//                log.error("Error sending message to session: " + session.getId(), e);
//                throw new CustomException(ErrorCode.WEBSOCKET_SEND_ERROR);
//            }
//        });
//
//
//    }

//--------------------------------------------------------------------------------------------------------------------------------------------//

    private Gym findGym(Long gymId) {
        return gymRepository.findById(gymId)
            .orElseThrow(() -> new CustomException(ErrorCode.GYM_NOT_FOUND));
    }

    public Chat findChat(Long chatId) {
        return chatRepository.findById(chatId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_NOT_FOUND));
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateUserMembership(User user, Gym gym) {
        if (!userGymRepository.existsByUserAndGym(user, gym)) {
            throw new CustomException(ErrorCode.NOT_MY_GYM);
        }
    }

    public void validateChatIsMyGym(User user, Gym gym) {
        if (!userGymRepository.existsByUserAndGym(user, gym)) {
            throw new CustomException(ErrorCode.CHAT_NOT_MY_GYM);
        }
    }

    private void validateDuplicatedUserChat(User user, Chat chat) {
        if (userChatRepository.existsByChatAndUser(chat, user)) {
            throw new CustomException(ErrorCode.DUPLICATED_USER_CHAT);
        }
    }

    public void validateJoinedChat(User user, Chat chat) {
        if (!userChatRepository.existsByChatAndUser(chat, user)) {
            throw new CustomException(ErrorCode.USER_CHAT_NOT_FOUND);
        }
    }

    private void validateUserIsChatOwner(User user, Chat chat) {
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
            .orElseThrow(() -> new CustomException(ErrorCode.USER_CHAT_NOT_FOUND));
    }

//    private String convertMessageToJson(MessageResponseDto message) {
//        try {
//            return objectMapper.writeValueAsString(message);
//        } catch (JsonProcessingException e) {
//            log.error("Error converting message to JSON", e);
//            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
//        }
//    }

//    private MessageResponseDto changeToResponseDto(MessageRequestDto message) {
//        String senderNickname = findUser(message.getSenderId()).getNickname();
//
//        MessageResponseDto messageResponseDto = MessageResponseDto.builder()
//            .chatId(message.getChatId())
//            .senderNickname(senderNickname)
//            .content(message.getContent())
//            .messageDate(LocalDateTime.now())
//            .build();
//
//        return messageResponseDto;
//    }

}
