package ru.otus.erinary.ms.dataserver.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.erinary.ms.dataserver.dao.UserModel;
import ru.otus.erinary.ms.dataserver.orm.DBService;
import ru.otus.erinary.ms.messageserver.message.Message;
import ru.otus.erinary.ms.messageserver.message.create.CreateUserRequest;
import ru.otus.erinary.ms.messageserver.message.create.CreateUserResponse;
import ru.otus.erinary.ms.messageserver.message.load.LoadUsersRequest;
import ru.otus.erinary.ms.messageserver.message.load.LoadUsersResponse;
import ru.otus.erinary.ms.messageserver.message.util.ErrorMessage;
import ru.otus.erinary.ms.messageserver.service.MessageListener;
import ru.otus.erinary.ms.messageserver.service.SocketClient;

import java.util.stream.Collectors;

/**
 * Сервис для обработки запросов в БД от MessageServer
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataService implements MessageListener {

    private final DBService<UserModel> dbService;
    private final SocketClient socketClient;
    @Setter
    private String serverName;

    /**
     * Обработка входящего сообщения от MessageServer
     *
     * @param message сообщение от MessageServer
     */
    @Override
    public void handleMessage(Message message) {
        log.info("Starting to handle message for DBService [{}]", serverName);
        Message response;
        if (message instanceof CreateUserRequest) {
            response = createUser(message);
        } else if (message instanceof LoadUsersRequest) {
            response = loadUsers();
        } else {
            response = new ErrorMessage("Unknown message type for DBService");
        }
        response.setPutToQueue(message.getReplyTo());
        response.setWebSocketSessionId(message.getWebSocketSessionId());
        log.info("Response message from DataService: {}", response);
        socketClient.sendMessage(response);
    }

    private CreateUserResponse createUser(Message request) {
        log.info("Trying to create new user...");
        dbService.create(new UserModel(((CreateUserRequest) request).getUser()));
        log.info("New user is created");
        return new CreateUserResponse("OK", "User created");
    }

    private LoadUsersResponse loadUsers() {
        log.info("Trying to load users...");
        return new LoadUsersResponse(dbService.loadAll().stream().map(UserModel::toUser).collect(Collectors.toList()));
    }

}
