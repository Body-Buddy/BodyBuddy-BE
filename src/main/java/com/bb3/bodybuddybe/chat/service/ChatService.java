package com.bb3.bodybuddybe.chat.service;

import com.bb3.bodybuddybe.chat.dto.ChatRequestDto;
import com.bb3.bodybuddybe.chat.dto.ChatResponseDto;
import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.repository.ChatRepository;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;
    private final GymRepository gymRepository;

    // 채팅방 생성
    @Transactional
    public ChatResponseDto createChatRoom(Users user, ChatRequestDto chatRequestDto, Long gymId) {

        Gym gym = gymRepository.findById(gymId).orElseThrow(() ->
            new IllegalArgumentException("해당 Gym을 찾을 수 업습니다."));

        Chat chat = Chat.builder()
            .roomName(chatRequestDto.getRoomName())
            .chatType(chatRequestDto.getChatType())
            .gym(gym)
            .build();

        chatRepository.save(chat);


        return new ChatResponseDto(chat.getId(), chat.getRoomName(), chat.getChatType());

    }


}
