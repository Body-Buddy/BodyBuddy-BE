package com.bb3.bodybuddybe.chat.service;

import com.bb3.bodybuddybe.chat.dto.MessageRequestDto;
import com.bb3.bodybuddybe.chat.dto.MessageResponseDto;
import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.chat.repository.MessageRepository;
import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;

import com.bb3.bodybuddybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatService chatService;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public List<MessageResponseDto> getPastMessages(Long chatId, User user) {
        Chat chat = chatService.findChat(chatId);
        chatService.validateJoinedChat(user, chat);

        return messageRepository.findAllByChatId(chatId)
                .stream()
                .map(MessageResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageResponseDto sendMessage(Long chatId, MessageRequestDto requestDto) {
        Message message = Message.builder()
                .content(requestDto.getContent())
                .sender(findUser(requestDto.getSenderId()))
                .chat(chatService.findChat(chatId))
                .build();

        messageRepository.save(message);

        return new MessageResponseDto(message);
    }

    public MessageResponseDto sendEnterMessage(Long chatId, MessageRequestDto requestDto) {
        Chat chat = chatService.findChat(chatId);
        User user = findUser(requestDto.getSenderId());
        chatService.validateDuplicatedChat(user, chat);

        Message message = Message.builder()
                .content(user.getNickname() + "님이 입장하셨습니다.")
                .sender(user)
                .chat(chatService.findChat(chatId))
                .build();

        messageRepository.save(message);

        return new MessageResponseDto(message);
    }

    public MessageResponseDto sendLeaveMessage(Long chatId, MessageRequestDto requestDto) {
        Chat chat = chatService.findChat(chatId);
        User user = findUser(requestDto.getSenderId());
        chatService.validateJoinedChat(user, chat);

        Message message = Message.builder()
                .content(user.getNickname() + "님이 퇴장하셨습니다.")
                .sender(user)
                .chat(chatService.findChat(chatId))
                .build();

        messageRepository.save(message);

        return new MessageResponseDto(message);
    }

    private User findUser(Long senderId) {
        return userRepository.findById(senderId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
