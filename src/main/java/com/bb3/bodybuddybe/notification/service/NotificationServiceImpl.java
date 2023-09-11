package com.bb3.bodybuddybe.notification.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.like.entity.PostLike;
import com.bb3.bodybuddybe.notification.dto.NotificationListResponseDto;
import com.bb3.bodybuddybe.notification.dto.NotificationRequestDto;
import com.bb3.bodybuddybe.notification.dto.NotificationResponseDto;
import com.bb3.bodybuddybe.notification.entity.Notification;
import com.bb3.bodybuddybe.notification.entity.NotificationType;
import com.bb3.bodybuddybe.notification.repository.EmitterRepositoryImpl;
import com.bb3.bodybuddybe.notification.repository.NotificationRepository;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // SSE 유효시간 : 1시간
    public static final String CLIENT_BASIC_URL = "https://localhost:8080/api";
    private final NotificationRepository notificationRepository;
    private final EmitterRepositoryImpl emitterRepository;

    @Override
    @Transactional
    public SseEmitter subscribe(User user, String lastEventId) {
        String emitterId = makeTimeIncludeId(user);
        SseEmitter sseEmitter = emitterRepository.saveEmitter(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeId(user);
        sendToClient(sseEmitter, emitterId, eventId,
                "연결되었습니다. EventStream Created. [userId=" + user.getId() + "]");

        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithUserId(
                    String.valueOf(user.getId()));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(sseEmitter, entry.getKey(), entry.getKey(), entry.getValue())
                    );
        }
        return sseEmitter;
    }

    private String makeTimeIncludeId(User user) {  // 데이터 유실 시점 파악 위함
        return user.getId() + "_" + System.currentTimeMillis();
    }

    // 특정 SseEmitter 를 이용해 알림을 보냅니다. SseEmitter 는 최초 연결 시 생성되며,
    // 해당 SseEmitter 를 생성한 클라이언트로 알림을 발송하게 됩니다.
    @Override
    public void sendToClient(SseEmitter emitter, String emitterId, String eventId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .name("sse")
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            throw new CustomException(ErrorCode.SSE_CONNECTION_FAILED);
        }
    }

    @Override
    public void send(NotificationRequestDto requestDto) {
        sendNotification(requestDto, saveNotification(requestDto));
    }

    //알림 보내기
    @Async
    public void sendNotification(NotificationRequestDto requestDto, Notification notification) {
        String receiverId = String.valueOf(requestDto.getReceiver().getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();

        Map<String, SseEmitter> emitters = emitterRepository
                .findAllEmitterStartWithByUserId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    //데이터 캐시 저장
                    emitterRepository.saveEventCache(key, notification);
                    //데이터 전송
                    sendToClient(emitter, key, eventId, NotificationResponseDto.of(notification));

                }
        );
    }

    @Transactional
    public Notification saveNotification(NotificationRequestDto requestDto) {
        Notification notification = Notification.builder()
                .receiver(requestDto.getReceiver())
                .notificationType(requestDto.getNotificationType())
                .message(requestDto.getMessage())
                .isRead(false)
                .build();
        notificationRepository.save(notification);
        return notification;
    }

    @Override
    @Transactional
    public void notifyToUsersThatTheyHaveReceivedLike(PostLike postLike) {
        User receiver = postLike.getPost().getAuthor(); // 글쓴이
        String message =
                postLike.getUser().getUsername() + "님이 \""
                        + postLike.getPost().getTitle() + "\" 게시글에 대해 좋아요를 눌렀습니다.";

//        String redirectUrl = CLIENT_BASIC_URL + "/posts/" + likePost.getPost().getId();

        NotificationRequestDto requestDto = NotificationRequestDto.builder()
                .notificationType(NotificationType.POST_LIKE)
                .message(message)
                .receiver(receiver)
                .build();

        send(requestDto);
    }

    /**
     * 알림 목록 조회
     *
     * @param user
     */
    @Override
    public NotificationListResponseDto getNotifications(User user) {
        return NotificationListResponseDto.of(
                notificationRepository.findAllByReceiverId(user.getId()));
    }

    /**
     * 알림 읽음 처리
     *
     * @param notificationId
     * @param user
     */
    @Override
    @Transactional
    public void readNotification(Long notificationId, User user) {
        Notification notification = findNotification(notificationId);
        notification.read();
        notificationRepository.save(notification);
    }

    /**
     * 알림 삭제
     *
     * @param notificationId
     * @param user
     */
    @Override
    @Transactional
    public void deleteNotification(Long notificationId, User user) {
        notificationRepository.delete(findNotification(notificationId));
    }

    /**
     * 알림 존재 여부
     *
     * @param id
     * @return
     */
    @Override
    public Notification findNotification(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }
}
