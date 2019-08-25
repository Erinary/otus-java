package ru.otus.erinary.messagesystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.otus.erinary.messagesystem.message.Message;
import ru.otus.erinary.messagesystem.service.MessageSystemClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final String dataBaseServiceQueueName = "TO_DBSERVICE";
    private final String frontendQueueName = "TO_FRONTEND";

    private final MessageSystemClient messageService;
    private final ObjectMapper objectMapper;
    private MessageDeliveryThread deliveryThread;
    private ConcurrentHashMap<String, WebSocketSession> webSocketMap = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            Message request = objectMapper.readValue(message.getPayload(), Message.class);
            log.info("Got message from client: {}", message);
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

    @PostConstruct
    private void postConstruct() {
        deliveryThread = this.new MessageDeliveryThread();
        deliveryThread.start();
    }

    @PreDestroy
    private void preDestroy() {
        deliveryThread.interrupt();
    }

    private class MessageDeliveryThread extends Thread {
        @Override
        public void run() {
            log.info("MessageDeliveryThread run");
            while (true) {
                try {
                    Message message = messageService.getMessageFromQueue(frontendQueueName);
                    WebSocketSession session = webSocketMap.get(message.getSessionId());
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
                } catch (InterruptedException e) {
                    log.info("MessageDeliveryThread is interrupted");
                    break;
                } catch (Exception e) {
                    log.error("Failed to send response ", e);
                }
            }
        }
    }
}
