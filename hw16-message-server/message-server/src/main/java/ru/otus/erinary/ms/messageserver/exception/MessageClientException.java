package ru.otus.erinary.ms.messageserver.exception;

public class MessageClientException extends RuntimeException {

    public MessageClientException(String message) {
        super(message);
    }

    public MessageClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
