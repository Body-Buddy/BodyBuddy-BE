package com.bb3.bodybuddybe.chat.controller;

import com.bb3.bodybuddybe.chat.dto.ChatRequestDto;
import com.bb3.bodybuddybe.chat.dto.ChatResponseDto;
import com.bb3.bodybuddybe.chat.service.ChatService;
import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    // 채팅방 생성 (그룹방)
    @PostMapping("/gyms/{gymId}/chats")
    public ResponseEntity<ApiResponseDto> createChatRoom(@PathVariable Long gymId,
                                                         @Valid @RequestBody ChatRequestDto requestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatService.createGroupChat(gymId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("채팅방 생성 완료", HttpStatus.OK.value()));
    }

    // GYM 내 채팅방 전체목록 조회
    @GetMapping("/gyms/{gymId}/chats")
    public ResponseEntity<List<ChatResponseDto>> getAllChatsInGym(@PathVariable Long gymId) {
        List<ChatResponseDto> response = chatService.getAllChatsInGym(gymId);
        return ResponseEntity.ok(response);
    }

    // Gym 내에 내가 참여한 채팅방 목록 조회
    @GetMapping("/gym/{gymId}/users/{userId}/chats")
    public ResponseEntity<List<ChatResponseDto>> getMyChatsInGym(@PathVariable Long gymId,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ChatResponseDto> response = chatService.getMyChatsInGym(gymId, userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    // 1대1 채팅방 생성 또는 가져오기
    @PostMapping("/gyms/{gymId}/direct-chats/users/{toChatUserId}")
    public ResponseEntity<ChatResponseDto> getOrCreateDirectChat(@PathVariable Long gymId,
                                                                 @PathVariable Long toChatUserId,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChatResponseDto response = chatService.getOrCreateDirectChat(gymId, userDetails.getUser(), toChatUserId);
        return ResponseEntity.ok(response);
    }

    // 채팅방 수정(방이름 or ChatType)
    @PutMapping("/chats/{chatId}")
    public ResponseEntity<ApiResponseDto> updateChat(@PathVariable Long chatId,
                                                     @RequestBody ChatRequestDto chatRequestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatService.updateChat(userDetails.getUser(), chatId, chatRequestDto);
        return ResponseEntity.ok(new ApiResponseDto("채팅방 수정 완료", HttpStatus.OK.value()));
    }

    // 채팅방 삭제
    @DeleteMapping("/chats/{chatId}")
    public ResponseEntity<ApiResponseDto> deleteChat(@PathVariable Long chatId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatService.deleteChat(userDetails.getUser(), chatId);
        return ResponseEntity.ok(new ApiResponseDto("채팅방 삭제 완료", HttpStatus.OK.value()));
    }

    // 채팅방 참여
    @PostMapping("/chats/{chatId}/participants")
    public ResponseEntity<ApiResponseDto> joinChat(@PathVariable Long chatId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatService.joinChat(userDetails.getUser(), chatId);
        return ResponseEntity.ok(new ApiResponseDto("채팅방 참여 완료", HttpStatus.OK.value()));
    }

    // 채팅방 나가기
    @DeleteMapping("/chats/{chatId}/participants")
    public ResponseEntity<ApiResponseDto> leaveChat(@PathVariable Long chatId,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatService.leaveChat(userDetails.getUser(), chatId);
        return ResponseEntity.ok(new ApiResponseDto("채팅방 나가기 완료", HttpStatus.OK.value()));
    }
}
