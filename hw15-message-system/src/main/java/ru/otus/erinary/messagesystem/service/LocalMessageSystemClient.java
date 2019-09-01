package ru.otus.erinary.messagesystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.erinary.messagesystem.message.Message;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalMessageSystemClient implements MessageSystemClient {

    private final Map<String, MessageListener> queueListeners = new HashMap<>();
    private final Set<MessageHandlerThread> threadSet = new HashSet<>();
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

    @Override
    public void registerListener(MessageListener listener, String queueName) {
        if (queueListeners.containsKey(queueName)) {
            throw new RuntimeException("Listener for this queue already exists");
        }
        queueListeners.put(queueName, listener);
        MessageHandlerThread messageHandler = new MessageHandlerThread(queueName, listener);
        threadSet.add(messageHandler);
        messageHandler.start();
    }

    @PreDestroy
    public void shutdown() {
        threadSet.forEach(Thread::interrupt);
    }

    @RequiredArgsConstructor
    private class MessageHandlerThread extends Thread {

        private final String queueName;
        private final MessageListener messageListener;

        @Override
        public void run() {
            log.info("MessageHandlerThread run");
            while (true) {
                try {
                    log.info("Waiting message from queue '{}'", queueName);
                    Message request = getMessageFromQueue(queueName);
                    messageListener.handleMessage(request);
                } catch (InterruptedException e) {
                    log.info("MessageHandlerThread is interrupted");
                    break;
                } catch (Exception e) {
                    log.error("Failed to handle message: ", e);
                }
            }
        }
    }

}
