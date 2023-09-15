package com.bb3.bodybuddybe.notification.service;

import com.bb3.bodybuddybe.comment.entity.Comment;
import com.bb3.bodybuddybe.like.entity.PostLike;
import com.bb3.bodybuddybe.notification.dto.NotificationListResponseDto;
import com.bb3.bodybuddybe.notification.dto.NotificationRequestDto;
import com.bb3.bodybuddybe.notification.entity.Notification;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    void notifyToUsersThatTheyHaveReceivedLike(PostLike postLike);
    void notifyToUsersThatTheyHaveReceivedComment(Comment comment);
    SseEmitter subscribe(User user, String lastEventId);
    void send(NotificationRequestDto request);
    void sendToClient(SseEmitter emitter, String emitterId, String eventId, Object data);
    NotificationListResponseDto getNotifications(User user);
    void readNotification(Long notificationId, User user);
    void deleteNotification(Long notificationId, User user);
    Notification findNotification(Long notificationId);
}
