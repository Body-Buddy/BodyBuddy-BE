package com.bb3.bodybuddybe.chat.advice;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
@Getter
public class SessionManager {
    private Map<Long, Set<WebSocketSession>> sessions = new HashMap<>();

    public void addSession(Long chatId, WebSocketSession session) {
        this.sessions
            .computeIfAbsent(chatId,k -> new HashSet<>())
            .add(session);
    }

    public Long deleteSessionAndGetChatId(WebSocketSession session) {
        Long chatId = null;
        for (Map.Entry<Long, Set<WebSocketSession>> entry : sessions.entrySet()) {
            Long id = entry.getKey();
            Set<WebSocketSession> set = entry.getValue();

            if (set.contains(session)) {
                set.remove(session);
                chatId = id;
            }
        }
        return chatId;
    }

    public boolean isEntered(WebSocketSession session) {
        for (Set<WebSocketSession> set : sessions.values()) {
            if (set.contains(session)) {
             return true;
            }
        }
        return false;
    }

    public Set<WebSocketSession> getSessionsByChatId(Long chatId) {
        return sessions.getOrDefault(chatId, Collections.emptySet());
    }
}
