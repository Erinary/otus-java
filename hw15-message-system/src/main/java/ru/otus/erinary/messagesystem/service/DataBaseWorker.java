package ru.otus.erinary.messagesystem.service;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.erinary.messagesystem.message.*;
import ru.otus.erinary.messagesystem.model.ApiUser;
import ru.otus.erinary.model.User;
import ru.otus.erinary.orm.DBService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataBaseWorker {

    private final String dataBaseServiceQueueName;
    private final String frontendQueueName;

    private final DBService<User> dbService;
    private final MessageSystemClient messageService;
    private MessageHandlerThread messageHandler;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Builder
    public DataBaseWorker(String dataBaseServiceQueueName, String frontendQueueName, DBService<User> dbService, MessageSystemClient messageService) {
        this.dataBaseServiceQueueName = dataBaseServiceQueueName;
        this.frontendQueueName = frontendQueueName;
        this.dbService = dbService;
        this.messageService = messageService;
    }

    private Message handleMessage(Message request) {
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
        return response;
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

    @PostConstruct
    private void postConstruct() {
        messageHandler = this.new MessageHandlerThread();
        messageHandler.start();
    }

    @PreDestroy
    private void messageHandler() {
        messageHandler.interrupt();
    }

    private class MessageHandlerThread extends Thread {
        @Override
        public void run() {
            log.info("MessageHandlerThread run");
            while (true) {
                try {
                    Message request = messageService.getMessageFromQueue(dataBaseServiceQueueName);
                    messageService.queueMessage(handleMessage(request), frontendQueueName);
                } catch (InterruptedException e) {
                    log.info("MessageHandlerThread is interrupted");
                    break;
                } catch (Exception e) {
                    log.error("Failed to send response ", e);
                }
            }
        }
    }

}
