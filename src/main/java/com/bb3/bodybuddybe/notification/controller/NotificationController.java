package com.bb3.bodybuddybe.notification.controller;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.notification.dto.NotificationListResponseDto;
import com.bb3.bodybuddybe.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping(value = "/connect", produces = "text/event-stream")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return ResponseEntity.ok(notificationService.subscribe(userDetails.getUser(), lastEventId));
    }

    @GetMapping
    public ResponseEntity<NotificationListResponseDto> getNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(notificationService.getNotifications(userDetails.getUser()));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ApiResponseDto> readNotification(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long id) {
        notificationService.readNotification(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("알림을 읽었습니다!", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteNotification(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable Long id) {
        notificationService.deleteNotification(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("알림을 삭제했습니다!", HttpStatus.OK.value()));
    }

}
