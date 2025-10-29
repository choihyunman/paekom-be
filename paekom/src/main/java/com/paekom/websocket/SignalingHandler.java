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
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class SignalingHandler extends TextWebSocketHandler {
    private final RoomManager roomManager;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("✅ WebSocket connected: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JsonNode msg = objectMapper.readTree(payload);
        String type = msg.path("type").asText();
        String roomId = msg.path("roomId").asText();

        log.info("📩 Received message: type={} roomId={} from={}", type, roomId, session.getId());

        switch (type) {
            case "join" -> {
                roomManager.join(roomId, session);
                Set<WebSocketSession> peers = roomManager.peers(roomId);
                log.info("👥 [JOIN] session={} joined room={} (현재 인원: {})", session.getId(), roomId, peers.size());
            }
            case "leave", "end" -> {
                log.info("🚪 [LEAVE] session={} left room={}", session.getId(), roomId);
                relayToOthers(roomId, session, message);
                roomManager.leave(session);
            }
            case "offer", "answer", "ice" -> {
                log.debug("🛰️ [Relay] type={} room={} from={}", type, roomId, session.getId());
                relayToOthers(roomId, session, message);
            }
            default -> {
                log.warn("⚠️ Unknown message type received: {}", type);
            }
        }
    }

    private void relayToOthers(String roomId, WebSocketSession from, TextMessage message) throws IOException {
        for (WebSocketSession s : roomManager.peers(roomId)) {
            if (s.isOpen() && !s.getId().equals(from.getId())) {
                s.sendMessage(message);
                log.debug("🔁 Relayed message to session={} in room={}", s.getId(), roomId);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("❌ WebSocket closed: {} (status={})", session.getId(), status);
        roomManager.leave(session);
    }
}