package ru.otus.erinary.ms.messageserver;

import ru.otus.erinary.ms.messageserver.service.MessageServer;

import java.util.List;

public class MessageServerStarter {

    public static void main(String[] args) {
        try {
            new MessageServer(List.of("to-data-service", "to-web-service"), 0).run();
        } catch (Exception e) {
            System.exit(-1);
        }
    }
}
