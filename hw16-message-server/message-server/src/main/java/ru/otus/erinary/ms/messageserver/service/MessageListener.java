package ru.otus.erinary.ms.messageserver.service;

import ru.otus.erinary.ms.messageserver.message.Message;

public interface MessageListener {

    void handleMessage(Message message) throws Exception;

}
