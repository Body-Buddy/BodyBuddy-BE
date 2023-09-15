package com.bb3.bodybuddybe.chat.service;

import com.bb3.bodybuddybe.chat.dto.ChatRequestDto;
import com.bb3.bodybuddybe.chat.dto.ChatResponseDto;
import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.entity.ChatParticipant;
import com.bb3.bodybuddybe.chat.entity.ChatType;
import com.bb3.bodybuddybe.chat.repository.ChatRepository;
import com.bb3.bodybuddybe.chat.repository.ChatParticipantRepository;
import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.gym.repository.UserGymRepository;
import com.bb3.bodybuddybe.user.entity.User;
import com.bb3.bodybuddybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final UserGymRepository userGymRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    /*
     * 채팅방 CRUD
     * Used in ChatController
     */
    @Transactional
    public void createGroupChat(Long gymId, ChatRequestDto requestDto, User user) {
        Gym gym = findGym(gymId);
        validateUserMembership(user, gym);

        Chat chat = Chat.builder()
                .name(requestDto.getName())
                .chatType(ChatType.GROUP)
                .gym(gym)
                .owner(user)
                .build();

        ChatParticipant chatParticipant = new ChatParticipant(chat.getOwner(), chat);

        chatRepository.save(chat);
        chatParticipantRepository.save(chatParticipant);
    }

    @Transactional(readOnly = true)
    public List<ChatResponseDto> getAllChatsInGym(Long gymId) {
        return chatRepository.findAllByGym_Id(gymId)
                .stream()
                .map(ChatResponseDto::new)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<ChatResponseDto> getMyChatsInGym(Long gymId, User user) {
        return chatParticipantRepository.findAllByUserAndChat_Gym_Id(user, gymId)
                .stream()
                .map(ChatParticipant::getChat)
                .map(ChatResponseDto::new)
                .toList();
    }

    @Transactional
    public void updateChat(User user, Long chatId, ChatRequestDto requestDto) {
        Chat chat = findChat(chatId);
        validateUserIsChatOwner(user, chat);

        chat.updateChat(requestDto.getName());
    }

    @Transactional
    public void deleteChat(User user, Long chatId) {
        Chat chat = findChat(chatId);
        validateUserIsChatOwner(user, chat);

        chatRepository.delete(chat);
    }

    @Transactional
    public ChatResponseDto getOrCreateDirectChat(Long gymId, User user, Long toChatUserId) {
        User toChatUser = findUser(toChatUserId);
        Gym gym = findGym(gymId);

        validateUserMembership(user, gym);

        Chat directChat = chatRepository.findDirectChatBetweenUsers(gymId, user.getId(), toChatUserId);

        if(directChat == null) {
            Chat chat = Chat.builder()
                    .chatType(ChatType.DIRECT)
                    .name(toChatUser.getNickname() + "님과의 채팅")
                    .gym(gym)
                    .build();

            chatRepository.save(chat);

            ChatParticipant chatParticipant1 = new ChatParticipant(user, chat);
            ChatParticipant chatParticipant2 = new ChatParticipant(toChatUser, chat);

            chatParticipantRepository.save(chatParticipant1);
            chatParticipantRepository.save(chatParticipant2);

            directChat = chat;
        }

        return new ChatResponseDto(directChat);
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
        ChatParticipant chatParticipant = new ChatParticipant(user, chat);

        chatParticipantRepository.save(chatParticipant);
    }

    @Transactional
    public void leaveChat(User user, Long chatId) {
        Chat chat = findChat(chatId);

        if(chat.getChatType() == ChatType.GROUP) {
            ownerCantLeave(user, chat);
        }

        ChatParticipant chatParticipant = findChatParticipant(user, chatId);
        chatParticipantRepository.delete(chatParticipant);
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
        if (chatParticipantRepository.existsByChatAndUser(chat, user)) {
            throw new CustomException(ErrorCode.DUPLICATED_USER_CHAT);
        }
    }

    public void validateJoinedChat(User user, Chat chat) {
        if (!chatParticipantRepository.existsByChatAndUser(chat, user)) {
            throw new CustomException(ErrorCode.USER_CHAT_NOT_FOUND);
        }
    }

    private void validateUserIsChatOwner(User user, Chat chat) {
        if (!Objects.equals(chat.getOwner().getId(), user.getId())) {
            throw new CustomException(ErrorCode.USER_NOT_CHAT_OWNER);
        }
    }

    private void ownerCantLeave(User user, Chat chat) {
        if (Objects.equals(chat.getOwner().getId(), user.getId())) {
            throw new CustomException(ErrorCode.OWNER_CAN_NOT_LEAVE);
        }
    }

    private ChatParticipant findChatParticipant(User user, Long chatId) {
        return chatParticipantRepository.findByUserAndChatId(user, chatId)
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
