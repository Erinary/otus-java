package ru.otus.erinary.messagesystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.otus.erinary.messagesystem.message.Message;
import ru.otus.erinary.messagesystem.service.MessageListener;
import ru.otus.erinary.messagesystem.service.MessageSystemClient;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SocketHandler extends TextWebSocketHandler implements MessageListener {

    private final String dataBaseServiceQueueName;

    private final MessageSystemClient messageService;
    private final ObjectMapper objectMapper;
    private ConcurrentHashMap<String, WebSocketSession> webSocketMap = new ConcurrentHashMap<>();

    @Builder
    public SocketHandler(String dataBaseServiceQueueName, MessageSystemClient messageService, ObjectMapper objectMapper) {
        this.dataBaseServiceQueueName = dataBaseServiceQueueName;
        this.messageService = messageService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            Message request = objectMapper.readValue(message.getPayload(), Message.class);
            log.info("Got message from client: {}", request);
            request.setSessionId(session.getId());
            messageService.queueMessage(request, dataBaseServiceQueueName);
        } catch (InterruptedException e) {
            log.info("Can not put message into queue. Websocket thread is interrupted");
        } catch (Exception e) {
            log.error("Failed to send response ", e);
            session.sendMessage(new TextMessage("Error: " + e.getMessage()));
        }
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
        WebSocketSession session = webSocketMap.get(message.getSessionId());
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
    }

}
