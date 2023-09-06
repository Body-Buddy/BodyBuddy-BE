package com.bb3.bodybuddybe.notification.dto;

import com.bb3.bodybuddybe.notification.entity.Notification;
import com.bb3.bodybuddybe.notification.entity.NotificationType;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationRequestDto {

    private String message;
    private User Receiver;
    private NotificationType notificationType;

    public Notification toEntity(NotificationType notificationType, User receiver) {
        return Notification.builder()
                .message(this.message)
                .receiver(receiver)
                .notificationType(notificationType)
                .isRead(false)
                .build();
    }

}
