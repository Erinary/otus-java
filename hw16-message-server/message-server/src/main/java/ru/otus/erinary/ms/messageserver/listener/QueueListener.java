package ru.otus.erinary.ms.messageserver.listener;

import lombok.extern.slf4j.Slf4j;
import ru.otus.erinary.ms.messageserver.service.QueueSinkRegistry;
import ru.otus.erinary.ms.messageserver.message.Message;

import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

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
                message = queue.take();
                ObjectOutputStream outputStream = queueOutputStreams.selectRandomSink(queueName);
                outputStream.writeObject(message);
                outputStream.flush();
            } catch (InterruptedException e) {
                throw e;
            } catch (Exception e) {
                log.error("Couldn't handle message, try to return message into queue. Error: ", e);
                if (message != null) {
                    queue.put(message);
                }
            }
        }
    }


}
