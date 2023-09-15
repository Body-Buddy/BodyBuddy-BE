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
import java.util.Objects;
import java.util.stream.Collectors;

import com.bb3.bodybuddybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatService chatService;

    public MessageResponseDto participation(Long chatId, MessageRequestDto messageRequestDto, User user) {
        Chat chat = chatService.findChat(chatId);
        if (!Objects.equals(user.getId(), messageRequestDto.getSenderId())) {
            throw new CustomException(ErrorCode.NOT_SAME_LOGIN_USER);
        }
        chatService.validateChatIsMyGym(user, chat.getGym());
        chatService.validateJoinedChat(user, chat);

        MessageRequestDto requestDto = MessageRequestDto.builder()
            .chatId(chatId)
            .senderId(messageRequestDto.getSenderId())
            .content(messageRequestDto.changeEnterMessage(user.getNickname()))
            .build();

        MessageResponseDto responseDto = requestDto.changeToResponseDto(user);

        return responseDto;
    }

    @Transactional
    public MessageResponseDto sendMessage(Long chatId, MessageRequestDto messageRequestDto) {
        User user = findUser(messageRequestDto.getSenderId());

        MessageRequestDto requestDto = MessageRequestDto.builder()
            .chatId(chatId)
            .senderId(messageRequestDto.getSenderId())
            .content(messageRequestDto.getContent())
            .build();

        Message message = Message.builder()
            .content(requestDto.getContent())
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

    public MessageResponseDto exit(Long chatId, MessageRequestDto messageRequestDto, User user) {

        MessageRequestDto requestDto = MessageRequestDto.builder()
            .chatId(chatId)
            .senderId(messageRequestDto.getSenderId())
            .content(messageRequestDto.changeExitMessage(user.getNickname()))
            .build();

        MessageResponseDto messageResponseDto = requestDto.changeToResponseDto(user);

        return messageResponseDto;
    }

    public List<MessageResponseDto> getPastMessages(Long chatId) {
        List<MessageResponseDto> messageResponseDtos = messageRepository.findAllByChatId(chatId)
            .stream()
            .map(MessageResponseDto::new)
            .collect(Collectors.toList());
        return messageResponseDtos;
    }
}
