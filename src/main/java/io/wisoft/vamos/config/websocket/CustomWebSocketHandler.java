package io.wisoft.vamos.config.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CustomWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomWebSocketHandler.class);

    private final List<WebSocketSession> webSocketSessionList = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessionList.add(session);
        LOGGER.info("connection session: {}	", session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSessionList.remove(session);
        super.afterConnectionClosed(session, status);
    }

    //invokes when websocket message arrives
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        webSocketSessionList.forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(message);
                LOGGER.info("handleTextMessage arrived");
            } catch(IOException e) {
                LOGGER.error("handleTextMessage error occurred", e);
            }
        });
    }
}
