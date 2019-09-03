package ru.otus.erinary.messagesystem.service;

import ru.otus.erinary.messagesystem.message.Message;

public interface MessageSystemClient {

    void queueMessage(Message message, String queueName) throws InterruptedException;

    Message getMessageFromQueue(String queueName) throws InterruptedException;

    void registerListener(MessageListener listener, String queueName);

}
