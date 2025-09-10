package com.paekom.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class SignalingHandler extends TextWebSocketHandler {
    private final RoomManager roomManager;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.debug("WebSocket connected: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode msg = objectMapper.readTree(message.getPayload());
        String type = msg.path("type").asText();
        String roomId = msg.path("roomId").asText();

        switch (type) {
            case "join" -> {
                roomManager.join(roomId, session);
                log.warn("join room={} session={}", roomId, session.getId());
            }
            case "leave", "end" -> {
                relayToOthers(roomId, session, message);
                roomManager.leave(session);
            }
            case "offer", "answer", "ice" -> {
                relayToOthers(roomId, session, message);
            }
        }
    }

    private void relayToOthers(String roomId, WebSocketSession from, TextMessage message) throws IOException {
        for (WebSocketSession s : roomManager.peers(roomId)) {
            if (s.isOpen() && !s.getId().equals(from.getId())) {
                s.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.warn("WebSocket closed: {} status = {}", session.getId(), status);
        roomManager.leave(session);
    }
}
