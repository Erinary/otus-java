package ru.otus.erinary.ms.messageserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.erinary.ms.messageserver.exception.MessageClientException;
import ru.otus.erinary.ms.messageserver.message.Handshake;
import ru.otus.erinary.ms.messageserver.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

@Slf4j
public class SocketClient {

    private final String queueName;
    private final int port;
    private final String host;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private MessageListener messageListener;
    private MessageHandlerThread messageHandler;

    public SocketClient(String queueName, int port, String host) {
        this.queueName = queueName;
        this.port = port;
        this.host = host;
    }

    public void connect() {
        try {
            log.info("Connecting to server");
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            log.info("Sending handshake");
            outputStream.writeObject(new Handshake(queueName));
            outputStream.flush();
        } catch (IOException e) {
            log.error("Can not connect to server", e);
            shutdown();
            throw new MessageClientException("Failed to open socket", e);
        }
    }

    public void registerListener(MessageListener listener) {
        if (this.messageListener != null) {
            throw new RuntimeException("Listener for this client already exists");
        }
        messageListener = listener;
        messageHandler = new MessageHandlerThread(listener);
        messageHandler.start();
    }

    public void sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            throw new MessageClientException("Failed to send message", e);
        }
    }

    public void shutdown() {
        try {
            //TODO если не запущен MS, то при потыке запустить сервер валятся NPE
            messageHandler.interrupt();
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException ignored) {
        }
    }

    @RequiredArgsConstructor
    private class MessageHandlerThread extends Thread {

        private final MessageListener messageListener;

        @Override
        public void run() {
            log.info("MessageHandlerThread run");
            while (true) {
                try {
                    log.info("Waiting message from message server");
                    Message request = (Message) inputStream.readObject();
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
