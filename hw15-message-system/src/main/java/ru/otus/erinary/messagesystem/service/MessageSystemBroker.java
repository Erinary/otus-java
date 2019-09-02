package ru.otus.erinary.messagesystem.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.otus.erinary.messagesystem.message.Message;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Data
@Service
public class MessageSystemBroker {

    private static final int DEFAULT_QUEUE_CAPACITY = 100;
    private Map<String, BlockingQueue<Message>> queues;

    public MessageSystemBroker(List<String> queueList, int queueCapacity) {
        int capacity = queueCapacity > 0 ? queueCapacity : DEFAULT_QUEUE_CAPACITY;
        this.queues = queueList.stream()
                .collect(Collectors.toMap(queueName -> queueName, queueName -> new ArrayBlockingQueue<>(capacity)));
    }

    public void putToQueue(Message message, String queueName) throws InterruptedException {
        queues.get(queueName).put(message);
    }

    public Message takeFromQueue(String queueName) throws InterruptedException {
        return queues.get(queueName).take();
    }
}
