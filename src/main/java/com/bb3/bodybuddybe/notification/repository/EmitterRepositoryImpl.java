package com.bb3.bodybuddybe.notification.repository;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@NoArgsConstructor
public class EmitterRepositoryImpl implements EmitterRepository{
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();
}
