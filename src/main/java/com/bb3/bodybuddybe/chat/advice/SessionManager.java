package com.bb3.bodybuddybe.chat.advice;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
@Getter
public class SessionManager {
    private Set<WebSocketSession> sessions = new HashSet<>();


    public void addSession(WebSocketSession session) {
        this.sessions.add(session);
    }

    public void deleteSession(WebSocketSession session) {
        this.sessions.remove(session);
    }

}
