package ru.otus.erinary.ms.messageserver.service;

import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * Класс, который содержит мапу [имя очереди - выводной поток соответствующего сокета] и методы для работы с ней
 */
public class QueueSinkRegistry {

    private Map<String, Set<ObjectOutputStream>> queueOutputStreams;

    QueueSinkRegistry(List<String> queueList) {
        this.queueOutputStreams = queueList.stream()
                .collect(Collectors.toConcurrentMap(queueName -> queueName, queueName -> new CopyOnWriteArraySet<>()));
    }

    public synchronized void addSink(String queueName, ObjectOutputStream outputStream) {
        queueOutputStreams.get(queueName).add(outputStream);
        this.notifyAll();
    }

    public synchronized ObjectOutputStream selectRandomSink(String queueName) throws InterruptedException {
        while (queueOutputStreams.get(queueName).isEmpty()) {
            this.wait();
        }
        return getRandomOutputStream(queueOutputStreams.get(queueName));
    }

    public synchronized void removeSink(String queueName, ObjectOutputStream outputStream) {
        queueOutputStreams.get(queueName).remove(outputStream);
    }

    private ObjectOutputStream getRandomOutputStream(Set<ObjectOutputStream> streamSet) {
        for (int i = 0; i < new Random().nextInt(streamSet.size()); i++) {
            streamSet.iterator().next();
        }
        return streamSet.iterator().next();
    }
}
