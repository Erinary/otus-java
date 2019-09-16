package ru.otus.erinary.ms.messageserver.exception;

public class MessageServerException extends RuntimeException {

    public MessageServerException(String message) {
        super(message);
    }

    public MessageServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
