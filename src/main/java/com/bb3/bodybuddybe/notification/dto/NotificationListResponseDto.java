package com.bb3.bodybuddybe.notification.dto;

import com.bb3.bodybuddybe.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class NotificationListResponseDto {
    private List<NotificationResponseDto> NotificationList;

    public static NotificationListResponseDto of(List<Notification> notifications) {
        return NotificationListResponseDto.builder()
                .NotificationList(notifications.stream().map(NotificationResponseDto::of)
                        .toList())
                .build();
    }
}