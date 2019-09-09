package ru.otus.erinary.ms.messageserver.listener;

import lombok.extern.slf4j.Slf4j;
import ru.otus.erinary.ms.messageserver.service.QueueSinkRegistry;
import ru.otus.erinary.ms.messageserver.exception.MessageServerException;
import ru.otus.erinary.ms.messageserver.message.Handshake;
import ru.otus.erinary.ms.messageserver.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Класс, содержащий в себе сокет, который открывается сокет-сервером при создании соединения, и который обрабатывает
 * входящие в MessageServer сообщения от других сервисов
 */
@Slf4j
public class SocketListener extends Thread {

    private final Socket socket;
    private final Map<String, BlockingQueue<Message>> queues;
    private final QueueSinkRegistry queueOutputStreams;

    public SocketListener(Socket socket, Map<String, BlockingQueue<Message>> queues, QueueSinkRegistry queueOutputStreams) {
        this.socket = socket;
        this.queues = queues;
        this.queueOutputStreams = queueOutputStreams;
    }

    @Override
    public void run() {
        log.info("Running socket handling thread");
        String getFromQueue = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        log.info("Opening input stream");
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            log.info("Waiting for handshake");
            Object handshake = inputStream.readObject();
            log.info("First object received");
            if (handshake instanceof Handshake) {
                getFromQueue = ((Handshake) handshake).getGetFromQueue();
                queueOutputStreams.addSink(getFromQueue, outputStream);
                log.info("Handshake is completed");
            } else {
                throw new MessageServerException("Handshake between client and message server failed");
            }
            //noinspection InfiniteLoopStatement
            while (true) {
                Object message = inputStream.readObject();
                String putToQueue = ((Message) message).getPutToQueue();
                queues.get(putToQueue).put((Message) message);
            }
        } catch (Exception e) {
            log.error("Error while handle opened socket", e);
        } finally {
            try {
                if (outputStream != null) {
                    queueOutputStreams.removeSink(getFromQueue, outputStream);
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

}
