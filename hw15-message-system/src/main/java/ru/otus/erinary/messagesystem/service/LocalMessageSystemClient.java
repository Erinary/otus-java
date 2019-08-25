package ru.otus.erinary.messagesystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.erinary.messagesystem.message.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalMessageSystemClient implements MessageSystemClient {

    private final MessageSystemBroker messageSystem;

    @Override
    public void queueMessage(Message message, String queueName) throws InterruptedException {
        log.info("Put message with id '{}' in '{}'", message.getSessionId(), queueName);
        messageSystem.putToQueue(message, queueName);
    }

    @Override
    public Message getMessageFromQueue(String queueName) throws InterruptedException {
        log.info("Waiting message from '{}'", queueName);
        return messageSystem.takeFromQueue(queueName);
    }

}
