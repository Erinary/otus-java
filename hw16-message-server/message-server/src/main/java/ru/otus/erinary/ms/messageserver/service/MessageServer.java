package ru.otus.erinary.ms.messageserver.service;

import lombok.extern.slf4j.Slf4j;
import ru.otus.erinary.ms.messageserver.listener.QueueListener;
import ru.otus.erinary.ms.messageserver.listener.SocketListener;
import ru.otus.erinary.ms.messageserver.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@Slf4j
public class MessageServer {

    private static final int PORT = 8888;
    private static final int DEFAULT_QUEUE_CAPACITY = 100;

    private final Map<String, BlockingQueue<Message>> queues;
    private final QueueSinkRegistry queueOutputStreams;
    private final ServerSocket serverSocket;

    public MessageServer(List<String> queueList, int queueCapacity) throws IOException {
        int capacity = queueCapacity > 0 ? queueCapacity : DEFAULT_QUEUE_CAPACITY;
        this.queues = queueList.stream()
                .collect(Collectors.toMap(queueName -> queueName, queueName -> new ArrayBlockingQueue<>(capacity)));
        this.queueOutputStreams = new QueueSinkRegistry(queueList);
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run() throws IOException {
        log.info("Message server started");
        try {
            initQueueListeners();
            //noinspection InfiniteLoopStatement
            while (true) {
                log.info("Waiting for connections");
                Socket socket = serverSocket.accept();
                log.info("Got new connection from {}", socket.getInetAddress());
                SocketListener socketListener = new SocketListener(socket, queues, queueOutputStreams);
                socketListener.start();
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Метод инициализирует свой QueueListener для каждой очереди сообщений (WebServer'ы имеют каждый свою очередь,
     * DataServer'ы - одну общую)
     */
    private void initQueueListeners() {
        queues.forEach((key, value) -> new QueueListener(key, value, queueOutputStreams).start());
    }

}
