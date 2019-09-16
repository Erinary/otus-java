package ru.otus.erinary.ms.messageserver.service;

import lombok.extern.slf4j.Slf4j;

import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * Класс, который содержит мапу [имя очереди - выводной поток соответствующего сокета] и методы для работы с ней
 */
@Slf4j
public class QueueSinkRegistry {

    private Map<String, Set<ObjectOutputStream>> queueOutputStreams;

    QueueSinkRegistry(List<String> queueList) {
        this.queueOutputStreams = queueList.stream()
                .collect(Collectors.toConcurrentMap(queueName -> queueName, queueName -> new CopyOnWriteArraySet<>()));
    }

    public synchronized void addSink(String queueName, ObjectOutputStream outputStream) {
        log.info("Adding sink for queue [{}]: [{}]", queueName, outputStream);
        queueOutputStreams.get(queueName).add(outputStream);
        this.notifyAll();
    }

    public synchronized ObjectOutputStream selectRandomSink(String queueName) throws InterruptedException {
        while (queueOutputStreams.get(queueName).isEmpty()) {
            this.wait();
        }
        log.info("All sinks for queue [{}]: {}", queueName, queueOutputStreams.get(queueName));
        ObjectOutputStream oos = getRandomOutputStream(queueOutputStreams.get(queueName));
        log.info("Selected sinks for queue [{}]: {}", queueName, oos);
        return oos;
    }

    public synchronized void removeSink(String queueName, ObjectOutputStream outputStream) {
        queueOutputStreams.get(queueName).remove(outputStream);
    }

    private ObjectOutputStream getRandomOutputStream(Set<ObjectOutputStream> streamSet) {
        return new ArrayList<>(streamSet).get((int) (streamSet.size() * Math.random()));
    }
}
