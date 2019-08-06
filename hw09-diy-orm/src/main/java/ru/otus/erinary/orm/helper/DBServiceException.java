package ru.otus.erinary.orm.helper;

public class DBServiceException extends RuntimeException {

    public DBServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBServiceException(String message) {
        super(message);
    }
}
