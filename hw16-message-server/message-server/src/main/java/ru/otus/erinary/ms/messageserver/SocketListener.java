package ru.otus.erinary.ms.messageserver;

import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class SocketListener extends Thread {

    private final Socket socket;
    private final Map<String, BlockingQueue<Message>> queues;
    private final Map<String, Set<Socket>> listeningSockets;

    public SocketListener(Socket socket, Map<String, BlockingQueue<Message>> queues, Map<String, Set<Socket>> listeningSockets) {
        this.socket = socket;
        this.queues = queues;
        this.listeningSockets = listeningSockets;
    }

    @Override
    public void run() {
        super.run();
    }

}
