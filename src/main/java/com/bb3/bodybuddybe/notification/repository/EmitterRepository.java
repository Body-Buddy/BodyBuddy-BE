package com.bb3.bodybuddybe.notification.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter saveEmitter(String id, SseEmitter emitter);

    void saveEventCache(String eventCacheId, Object event);

    Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId);

    Map<String, Object> findAllEventCacheStartWithUserId(String userId);

    void deleteById(String id);

    void deleteAllEmitterStartWithUserId(String userId);

    void deleteAllEventCacheStartWithId(String userId);

}
