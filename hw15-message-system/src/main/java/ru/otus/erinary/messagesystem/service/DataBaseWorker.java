package ru.otus.erinary.messagesystem.service;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.erinary.messagesystem.message.*;
import ru.otus.erinary.messagesystem.model.ApiUser;
import ru.otus.erinary.model.User;
import ru.otus.erinary.orm.DBService;

import java.util.stream.Collectors;

@Slf4j
@Service
public class DataBaseWorker implements MessageListener {

    private final String frontendQueueName;

    private final DBService<User> dbService;
    private final MessageSystemClient messageService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Builder
    public DataBaseWorker(String frontendQueueName, DBService<User> dbService, MessageSystemClient messageService) {
        this.frontendQueueName = frontendQueueName;
        this.dbService = dbService;
        this.messageService = messageService;
    }

    @Override
    public void handleMessage(Message request) throws Exception {
        log.info("Starting to handle message for DataBaseWorker");
        Message response;
        if (request instanceof CreateUserRequest) {
            response = createUser(request);
        } else if (request instanceof LoadUsersRequest) {
            response = loadUsers();
        } else {
            response = new ErrorMessage("Unknown message type for DBService");
        }
        response.setSessionId(request.getSessionId());
        log.info("Response message from DataBaseWorker: {}", response);
        messageService.queueMessage(response, frontendQueueName);
    }

    private CreateUserResponse createUser(Message request) {
        log.info("Trying to create new user...");
        dbService.create(((CreateUserRequest) request).getData().toUser());
        return CreateUserResponse.builder()
                .status("OK")
                .message("User created")
                .build();
    }

    private LoadUsersResponse loadUsers() {
        log.info("Trying to load users...");
        return new LoadUsersResponse(dbService.loadAll().stream().map(ApiUser::new).collect(Collectors.toList()));
    }


}
