package com.bb3.bodybuddybe.chat.controller;

import com.bb3.bodybuddybe.chat.dto.ChatRequestDto;
import com.bb3.bodybuddybe.chat.dto.ChatResponseDto;
import com.bb3.bodybuddybe.chat.service.ChatService;
import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import java.util.List;
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
    @PostMapping("/chats/{gymId}")
    public ResponseEntity<ApiResponseDto> createChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestBody ChatRequestDto chatRequestDto,
                                                         @PathVariable Long gymId) {
        String roomName = chatService.createChatRoom(userDetails.getUser(), chatRequestDto, gymId).getRoomname();

        return ResponseEntity.ok(new ApiResponseDto("채팅방 생성 성공." + " 방 이름 : " + roomName, HttpStatus.OK.value()));
    }

    // GYM 내 채팅방 전체목록 조회
    @GetMapping("/chats/{gymId}")
    public ResponseEntity<List<ChatResponseDto>> getAllChatsByGym(@PathVariable Long gymId) {
        List<ChatResponseDto> response = chatService.getAllChatsByGym(gymId);
        return ResponseEntity.ok(response);
    }

    // Gym내에 내가 참여한 채팅방 목록 조회
    @GetMapping("/gym/{gymId}/chats")
    public ResponseEntity<List<ChatResponseDto>> getMyChats(@PathVariable Long gymId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ChatResponseDto> response = chatService.getMyChats(gymId, userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    // 1대1 채팅방 생성 또는 가져오기
    @PostMapping("/gym/{gymId}/direct-chats/{toChatUserId}")
    public ResponseEntity<ChatResponseDto> getOrCreateDirectChatRoom(@PathVariable Long gymId,
                                                                     @PathVariable Long toChatUserId,
                                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChatResponseDto chatResponseDto = chatService.getOrCreateDirectChatRoom(gymId, userDetails.getUser(), toChatUserId);
        return ResponseEntity.ok(chatResponseDto);
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
    @PostMapping("/chats/{chatId}/joiners")    // api수정 필요?
    public ResponseEntity<ApiResponseDto> joinChat(@PathVariable Long chatId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatService.joinChat(userDetails.getUser(), chatId);
        return ResponseEntity.ok(new ApiResponseDto("채팅방 참여 완료", HttpStatus.OK.value()));
    }

    // 채팅방 나가기
    @DeleteMapping("/chats/{chatId}/joiners")
    public ResponseEntity<ApiResponseDto> leaveChat(@PathVariable Long chatId,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatService.leaveChat(userDetails.getUser(), chatId);
        return ResponseEntity.ok(new ApiResponseDto("채팅방 미참여 적용완료", HttpStatus.OK.value()));
    }
}
