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
import ru.otus.erinary.ms.messageserver.message.create.CreateUserRequest;
import ru.otus.erinary.ms.messageserver.message.create.CreateUserResponse;
import ru.otus.erinary.ms.messageserver.message.load.LoadUsersRequest;
import ru.otus.erinary.ms.messageserver.message.load.LoadUsersResponse;
import ru.otus.erinary.ms.messageserver.message.util.ErrorMessage;
import ru.otus.erinary.ms.messageserver.service.MessageListener;
import ru.otus.erinary.ms.messageserver.service.SocketClient;
import ru.otus.erinary.ms.webserver.model.WebMessage;
import ru.otus.erinary.ms.webserver.model.create.WebCreateUserRequest;
import ru.otus.erinary.ms.webserver.model.create.WebCreateUserResponse;
import ru.otus.erinary.ms.webserver.model.load.WebLoadUsersRequest;
import ru.otus.erinary.ms.webserver.model.load.WebLoadUsersResponse;
import ru.otus.erinary.ms.webserver.model.util.WebErrorMessage;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервис по обработке запросов с веб-клиента и работе с MessageServer
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebService extends AbstractWebSocketHandler implements MessageListener {

    private final String listenedQueue;
    private final String targetQueue;
    private final SocketClient socketClient;
    private final ObjectMapper objectMapper;
    private ConcurrentHashMap<String, WebSocketSession> webSocketMap = new ConcurrentHashMap<>();

    /**
     * Обработка запросов клиента в веб-сокете
     *
     * @param session     сессия веб-сокета
     * @param textMessage запрос веб-клиента
     * @throws Exception ошибки при обработке запроса
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        WebMessage request = objectMapper.readValue(textMessage.getPayload(), WebMessage.class);
        log.info("Got message from client: {}", request);
        Message message;
        if (request instanceof WebCreateUserRequest) {
            message = new CreateUserRequest(((WebCreateUserRequest) request).getApiUser().toUser());
        } else if (request instanceof WebLoadUsersRequest) {
            message = new LoadUsersRequest();
        } else {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                    new WebErrorMessage("Unknown type of message from websocket"))));
            return;
        }
        message.setWebSocketSessionId(session.getId());
        message.setPutToQueue(targetQueue);
        message.setReplyTo(listenedQueue);
        socketClient.sendMessage(message);
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

    /**
     * Метод обработки ответов от MessageServer
     *
     * @param message сообщение от MessageServer
     * @throws Exception ошибки при обработке сообщения
     */
    @Override
    public void handleMessage(Message message) throws Exception {
        log.info("Got answer from MessageServer, sending it to client");
        WebSocketSession session = webSocketMap.get(message.getWebSocketSessionId());
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(convertMessage(message))));
    }

    private WebMessage convertMessage(Message message) {
        if (message instanceof CreateUserResponse) {
            return new WebCreateUserResponse(
                    ((CreateUserResponse) message).getStatus(),
                    ((CreateUserResponse) message).getMessage()
            );
        } else if (message instanceof LoadUsersResponse) {
            return new WebLoadUsersResponse(((LoadUsersResponse) message).getUsers());
        } else {
            return new WebErrorMessage(((ErrorMessage) message).getMessage());
        }
    }
}
