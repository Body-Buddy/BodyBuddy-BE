package com.bb3.bodybuddybe.notification.dto;

import com.bb3.bodybuddybe.notification.entity.Notification;
import com.bb3.bodybuddybe.notification.entity.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class NotificationResponseDto {
    private Long id;
    private String message;
    private NotificationType notificationType;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationResponseDto of(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .notificationType(notification.getNotificationType())
                .isRead(false)
                .createdAt(notification.getCreatedAt())
                .build();
    }


}
