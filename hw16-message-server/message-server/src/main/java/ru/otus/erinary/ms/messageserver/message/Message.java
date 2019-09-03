package ru.otus.erinary.ms.messageserver.message;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private String putToQueue;
    private String replyTo;

}
