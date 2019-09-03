package ru.otus.erinary.ms.messageserver;

import lombok.extern.slf4j.Slf4j;
import ru.otus.erinary.ms.messageserver.listener.SocketListener;
import ru.otus.erinary.ms.messageserver.message.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Slf4j
public class MessageServer {

    private static final int PORT = 8888;
    private static final int DEFAULT_QUEUE_CAPACITY = 100;

    private final Map<String, BlockingQueue<Message>> queues;
    private final Map<String, Set<ObjectOutputStream>> queueOutputStreams;
    private final ServerSocket serverSocket;

    public MessageServer(List<String> queueList, int queueCapacity) throws IOException {
        int capacity = queueCapacity > 0 ? queueCapacity : DEFAULT_QUEUE_CAPACITY;
        this.queues = queueList.stream()
                .collect(Collectors.toMap(queueName -> queueName, queueName -> new ArrayBlockingQueue<>(capacity)));
        this.queueOutputStreams = queueList.stream()
                .collect(Collectors.toConcurrentMap(queueName -> queueName, queueName -> new CopyOnWriteArraySet<>()));
        this.serverSocket = new ServerSocket(PORT);
    }

    public static void main(String[] args) {
        try {
            new MessageServer(List.of("to-data-service", "to-web-service"), 0).run();
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    public void run() throws IOException {
        log.info("Message server started");
        try {
            Socket socket = serverSocket.accept();
            SocketListener socketListener = new SocketListener(socket, queues, queueOutputStreams);
            socketListener.start();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw e;
        }
    }

}
