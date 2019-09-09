package ru.otus.erinary.ms.messageserver.listener;

import lombok.extern.slf4j.Slf4j;
import ru.otus.erinary.ms.messageserver.service.QueueSinkRegistry;
import ru.otus.erinary.ms.messageserver.message.Message;

import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

/**
 * Класс, который слушает очередь сообщений для определенного сервиса и обрабатывает положенные туда сообщения
 */
@Slf4j
public class QueueListener extends Thread {

    private String queueName;
    private BlockingQueue<Message> queue;
    private QueueSinkRegistry queueOutputStreams;

    public QueueListener(String queueName, BlockingQueue<Message> queue, QueueSinkRegistry queueOutputStreams) {
        this.queueName = queueName;
        this.queue = queue;
        this.queueOutputStreams = queueOutputStreams;
    }

    @Override
    public void run() {
        try {
            serverQueue();
        } catch (InterruptedException e) {
            this.interrupt();
            log.info("QueueListener [{}] was interrupted", queueName);
        }
    }

    private void serverQueue() throws InterruptedException {
        //noinspection InfiniteLoopStatement
        while (true) {
            Message message = null;
            try {
                log.info("Waiting for message in queue {}", this.queueName);
                message = queue.take();
                log.info("Selecting sink for message in queue {}", this.queueName);
                ObjectOutputStream outputStream = queueOutputStreams.selectRandomSink(queueName);
                outputStream.writeObject(message);
                outputStream.flush();
            } catch (InterruptedException e) {
                throw e;
            } catch (Exception e) {
                log.error("Couldn't handle message, trying to return message into queue. Error: ", e);
                if (message != null) {
                    queue.put(message);
                }
            }
        }
    }


}
