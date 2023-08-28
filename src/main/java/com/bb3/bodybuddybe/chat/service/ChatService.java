package com.bb3.bodybuddybe.chat.service;

import com.bb3.bodybuddybe.chat.dto.ChatRequestDto;
import com.bb3.bodybuddybe.chat.dto.ChatResponseDto;
import com.bb3.bodybuddybe.chat.dto.MessageRequestDto;
import com.bb3.bodybuddybe.chat.dto.MessageResponseDto;
import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.chat.repository.ChatRepository;
import com.bb3.bodybuddybe.chat.repository.MessageRepository;
import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.gym.repository.UserGymRepository;
import com.bb3.bodybuddybe.user.entity.User;
import com.bb3.bodybuddybe.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ObjectMapper objectMapper;
    private final ChatRepository chatRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final UserGymRepository userGymRepository;

    // 채팅방 생성
    @Transactional
    public ChatResponseDto createChatRoom(User user, ChatRequestDto chatRequestDto, Long gymId) {

        Gym gym = gymRepository.findById(gymId)
            .orElseThrow(() -> new CustomException(ErrorCode.GYM_NOT_FOUND));

        if (!userGymRepository.existsByUserAndGym(user, gym)) {
            throw new CustomException(ErrorCode.NOT_MY_GYM);
        }

        Chat chat = Chat.builder()
            .roomName(chatRequestDto.getRoomName())
            .chatType(chatRequestDto.getChatType())
            .gym(gym)
            .build();

        chatRepository.save(chat);

        return new ChatResponseDto(chat.getId(), chat.getRoomName(), chat.getChatType());

    }


    @Transactional
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            new IOException(e.getMessage());
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

    @Transactional
    public <T> void getMessages(WebSocketSession session, Long chatId) {
        List<MessageResponseDto> messageRequestDtos = messageRepository.findAllByChatId(chatId)
            .stream()
            .map(MessageResponseDto::new)
            .collect(Collectors.toList());
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageRequestDtos)));
        } catch (IOException e) {
            new IOException(e.getMessage());
        }
    }
}
