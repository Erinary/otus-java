package ru.otus.erinary.ms.webserver.service;

import ru.otus.erinary.ms.messageserver.service.SocketClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Spring-обертка для сокет-клиента
 */
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
