package com.paekom.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class RoomManager {
    private final ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> sessionToRoom = new ConcurrentHashMap<>();

    public void join(String roomId, WebSocketSession session) {
        rooms.computeIfAbsent(roomId, k -> new CopyOnWriteArraySet<>()).add(session);
        sessionToRoom.put(session.getId(), roomId);
    }

    public void leave(WebSocketSession session) {
        String roomId = sessionToRoom.remove(session.getId());
        if (roomId == null) return;
        Set<WebSocketSession> set = rooms.get(roomId);
        if (set != null) {
            set.remove(session);
            if (set.isEmpty()) rooms.remove(roomId);
        }
    }

    public Set<WebSocketSession> peers(String roomId) {
        return rooms.getOrDefault(roomId, new CopyOnWriteArraySet<>());
    }

    public String roomOf(WebSocketSession session) {
        return sessionToRoom.get(session.getId());
    }
}
