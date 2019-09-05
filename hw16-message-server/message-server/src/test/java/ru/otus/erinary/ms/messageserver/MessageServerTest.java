package ru.otus.erinary.ms.messageserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.ms.messageserver.message.create.CreateUserRequest;
import ru.otus.erinary.ms.messageserver.message.Message;
import ru.otus.erinary.ms.messageserver.service.ClientSocket;
import ru.otus.erinary.ms.messageserver.service.MessageServer;

import java.io.IOException;
import java.util.List;

public class MessageServerTest {

    private Thread server;

    @BeforeEach
    void startServer() {
        server = new Thread(() -> {
            try {
                new MessageServer(List.of("to-data-service", "to-web-service"), 0).run();
            } catch (IOException e) {
                System.exit(-1);
            }
        });
        server.start();
    }

    @Test
    void testConnectToServer() throws Exception {
        ClientSocket client = new ClientSocket("to-web-service", 8888, "localhost");
        client.connect();
        client.registerListener(message -> System.out.println("Message received"));
        Message message = new CreateUserRequest();
        message.setPutToQueue("to-web-service");
        client.sendMessage(message);
        Thread.sleep(500);
    }

}
