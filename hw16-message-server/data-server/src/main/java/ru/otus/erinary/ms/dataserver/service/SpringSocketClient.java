package ru.otus.erinary.ms.dataserver.service;

import ru.otus.erinary.ms.messageserver.service.SocketClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class SpringSocketClient extends SocketClient {

    public SpringSocketClient(String queueName, int port, String host) {
        super(queueName, port, host);
    }

    @PostConstruct
    @Override
    public void connect() {
        super.connect();
    }

    @PreDestroy
    @Override
    public void shutdown() {
        super.shutdown();
    }

}
