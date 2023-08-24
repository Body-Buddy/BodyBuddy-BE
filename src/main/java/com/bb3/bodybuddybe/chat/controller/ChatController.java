package com.bb3.bodybuddybe.chat.controller;

import com.bb3.bodybuddybe.chat.dto.ChatRequestDto;
import com.bb3.bodybuddybe.chat.service.ChatService;
import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chats/{gymId}")
    public ResponseEntity<ApiResponseDto> createChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestBody ChatRequestDto chatRequestDto,
                                                         @PathVariable Long gymId) {
        String roomName = chatService.createChatRoom(userDetails.getUser(), chatRequestDto, gymId).getRoomName();

        return ResponseEntity.ok()
            .body(new ApiResponseDto("채팅방 생성 성공." + " 방 이름 : " + roomName, HttpStatus.OK.value()));
    }
}
