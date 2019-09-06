package ru.otus.erinary.ms.webserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import ru.otus.erinary.ms.messageserver.message.Message;
import ru.otus.erinary.ms.messageserver.service.MessageListener;
import ru.otus.erinary.ms.messageserver.service.SocketClient;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebService extends AbstractWebSocketHandler implements MessageListener {

    private final String listenedQueue;
    private final String targetQueue;
    private final SocketClient socketClient;
    private final ObjectMapper objectMapper;
    private ConcurrentHashMap<String, WebSocketSession> webSocketMap = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Message request = objectMapper.readValue(message.getPayload(), Message.class);
        log.info("Got message from client: {}", request);
        request.setWebSocketSessionId(session.getId());
        request.setPutToQueue(targetQueue);
        request.setReplyTo(listenedQueue);
        socketClient.sendMessage(request);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Websocket '{}' is opened", session.getId());
        webSocketMap.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Websocket '{}' is closed", session.getId());
        webSocketMap.remove(session.getId());
    }

    @Override
    public void handleMessage(Message message) throws Exception {
        WebSocketSession session = webSocketMap.get(message.getWebSocketSessionId());
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
    }

}
