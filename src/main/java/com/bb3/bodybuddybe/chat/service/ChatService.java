package com.bb3.bodybuddybe.chat.service;

import com.bb3.bodybuddybe.chat.dto.ChatRequestDto;
import com.bb3.bodybuddybe.chat.dto.ChatResponseDto;
import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.entity.ChatParticipant;
import com.bb3.bodybuddybe.chat.entity.ChatType;
import com.bb3.bodybuddybe.chat.repository.ChatParticipantRepository;
import com.bb3.bodybuddybe.chat.repository.ChatRepository;
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

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final UserGymRepository userGymRepository;
    private final ChatParticipantRepository chatParticipantRepository;

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
                .toList();
    }


    @Transactional(readOnly = true)
    public List<ChatResponseDto> getMyChatsInGym(Long gymId, User user) {
        return chatParticipantRepository.findAllByUserAndChat_Gym_Id(user, gymId)
                .stream()
                .map(ChatParticipant::getChat)
                .map(ChatResponseDto::new)
                .toList();
    }

    public ChatResponseDto getChat(Long chatId, User user) {
        Chat chat = findChat(chatId);
        validateJoinedChat(user, chat);

        return new ChatResponseDto(chat);
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
    public Long getOrCreateDirectChat(Long gymId, User user, Long toChatUserId) {
        User toChatUser = findUser(toChatUserId);
        Gym gym = findGym(gymId);

        validateUserMembership(user, gym);

        Chat directChat = chatRepository.findDirectChatBetweenUsers(gymId, user.getId(), toChatUserId);

        if (directChat == null) {
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

        return directChat.getId();
    }

    @Transactional
    public void joinChat(User user, Long chatId) {
        Chat chat = findChat(chatId);
        validateDuplicatedChat(user, chat);
        ChatParticipant chatParticipant = new ChatParticipant(user, chat);

        chatParticipantRepository.save(chatParticipant);
    }

    @Transactional
    public void leaveChat(User user, Long chatId) {
        Chat chat = findChat(chatId);

        if (chat.getChatType() == ChatType.GROUP) {
            ownerCantLeave(user, chat);
        }

        ChatParticipant chatParticipant = findChatParticipant(user, chatId);
        chatParticipantRepository.delete(chatParticipant);
    }

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

    private ChatParticipant findChatParticipant(User user, Long chatId) {
        return chatParticipantRepository.findByUserAndChatId(user, chatId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_CHAT_NOT_FOUND));
    }

    private void validateUserMembership(User user, Gym gym) {
        if (!userGymRepository.existsByUserAndGym(user, gym)) {
            throw new CustomException(ErrorCode.NOT_MY_GYM);
        }
    }

    public void validateDuplicatedChat(User user, Chat chat) {
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
}
