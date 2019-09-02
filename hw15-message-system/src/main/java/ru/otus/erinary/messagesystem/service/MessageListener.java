package ru.otus.erinary.messagesystem.service;

import ru.otus.erinary.messagesystem.message.Message;

public interface MessageListener {

    void handleMessage(Message message) throws Exception;

}
